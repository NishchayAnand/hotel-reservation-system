package com.gharana.inventory_service.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.gharana.inventory_service.model.entity.InventoryRecord;
import com.gharana.inventory_service.model.enums.HoldStatus;
import com.gharana.inventory_service.exception.HoldUnavailableException;
import com.gharana.inventory_service.exception.InventoryUnavailableException;
import com.gharana.inventory_service.model.dto.AvailableRoomTypeDTO;
import com.gharana.inventory_service.model.dto.HoldDTO;
import com.gharana.inventory_service.model.dto.ReservationItemDTO;
import com.gharana.inventory_service.model.entity.Hold;
import com.gharana.inventory_service.model.entity.HoldItem;
import com.gharana.inventory_service.repository.HoldRepository;
import com.gharana.inventory_service.repository.InventoryRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private InventoryRepository inventoryRepository;
    private HoldRepository holdRepository;

    private static final Logger log = LoggerFactory.getLogger(InventoryServiceImpl.class);

    @Override
    public List<AvailableRoomTypeDTO> getAvailableRoomTypes(Set<Long> hotelIds, LocalDate checkInDate, LocalDate checkOutDate) { 
        // Fetch room types for the given hotels that have availability across the entire [checkInDate, checkOutDate) range.  
        long nights = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        List<Object[]> rows = inventoryRepository.findAvailableRoomTypesForHotels(hotelIds, checkInDate, checkOutDate, nights); 
        return rows.stream()
            .map(row -> new AvailableRoomTypeDTO( (Long) row[0], (Long) row[1], (int) row[2])) // row[0] => hotelId, row[1] => roomTypeId, row[2] => availableRoomCount
            .collect(Collectors.toList());
    }

    /*
     * Create a hold for the given reservation (idempotent by reservationId).
     * Throws InventoryUnavailableException for business limitation (no capacity).
     * Throws HoldUnavailableException if a previous hold existed but is RELEASED/EXPIRED.
    */
    @Override
    @Transactional
    public Hold createHold(
            Long reservationId, 
            Long hotelId, 
            LocalDate checkInDate, 
            LocalDate checkOutDate,
            List<ReservationItemDTO> reservationItems) {

        // Step 1: Idempotent Check: If hold already exists for this reservation, return or error depending on status
        Optional<Hold> existing = holdRepository.findByReservationId(reservationId);
        if(existing.isPresent()) {
            Hold hold = existing.get();
            String status = hold.getStatus().toString();
            if ("HELD".equals(status) || "CONFIRMED".equals(status)) {
                // idempotent success: return existing hold
                log.info("Returning existing hold for reservationId={} status={}", reservationId, status);
                return hold;
            } else {
                // RELEASED / EXPIRED -> cannot reuse; ask caller to create a new reservation request.
                log.warn("Attempt to recreate hold for reservationId={} which {}. Ignoring request.", reservationId, status);
                throw new HoldUnavailableException("Existing hold cannot be reused");
            }
        }

        // Step 2: For each reservation item (roomType), lock inventory for the date range and validate inventory 
        // Keep the locked rows per roomType so we can decrement and pesist later
        Map<Long, List<InventoryRecord>> lockedInventoryRecordsByRoomType = new HashMap<>();

        for(ReservationItemDTO item: reservationItems) {
            Long roomTypeId = item.roomTypeId();
            int requestedQuantity = item.quantity();

            // This method must be annotated with @Lock(PESSIMISTIC_WRITE) to lock the selected roomType inventory for the [checkInDate, checkOutDate).
            List<InventoryRecord> inventoryRecords = inventoryRepository.findForUpdateBetweenDates(hotelId, roomTypeId, checkInDate, checkInDate.minusDays(1));

            // Verify per-day availability.
            for(InventoryRecord inventoryRecord: inventoryRecords) {
                int availableQuantity = inventoryRecord.getTotalCount() - inventoryRecord.getReservedCount();
                if(availableQuantity < requestedQuantity) {
                    log.debug("Insufficient inventory for hotel={}, roomType={}, date={}, available={}, requested={}",
                        hotelId, roomTypeId, inventoryRecord.getReservationDate(), availableQuantity, requestedQuantity);
                    throw new InventoryUnavailableException("insufficient inventory for date " + inventoryRecord.getReservationDate());
                }
            }

            // All good for this roomType, decrement in-memory now
            for(InventoryRecord inventoryRecord: inventoryRecords) {
                inventoryRecord.setReservedCount(inventoryRecord.getReservedCount() + item.quantity());
            }

            lockedInventoryRecordsByRoomType.put(roomTypeId, inventoryRecords);
        
        }

        // Step 3: Persist updated inventory rows (still inside the same transaction)
        List<InventoryRecord> allInventoryRecordsToSave = lockedInventoryRecordsByRoomType.values().stream()
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
        inventoryRepository.saveAll(allInventoryRecordsToSave);

        for(SelectedInventoryRecords inventoryRecords: selectedInventoryRecords) {
            for(InventoryRecord record: inventoryRecords.records) {
                record.setReservedCount(record.getReservedCount() + inventoryRecords.selection.getCount());
                inventoryRepository.save(record);
            }
        }

        // Step 4: Create Hold object with list of HoldItems
        Hold hold = Hold.builder()
            .requestId(requestId)
            .status(HoldStatus.ACTIVE)
            .expiresAt(LocalDateTime.now().plusMinutes(15))
            .build();
        
        List<HoldItem> heldItems = new ArrayList<>();
        for(ReservationItemDTO selection: selectedInventory) {
            HoldItem item = HoldItem.builder()
                .hold(hold)
                .hotelId(hotelId)
                .checkInDate(checkInDate)
                .checkOutDate(checkOutDate)
                .roomTypeId(selection.getRoomTypeId())
                .heldCount(selection.getCount())
                .build();
            heldItems.add(item);
        }

        hold.setHeldItems(heldItems);

        // Step 5: Persist the Hold
        try {
            holdRepository.save(hold);
        } catch (DataIntegrityViolationException ex) {
            // Two concurrent requests with the same client holdId (idempotency key) can 
            // both pass the availability checks and inventory increments; 
            // the DB unique constraint on hold_id prevents the second insert and 
            // throws a DataIntegrityViolationException.
            Optional<Hold> maybe = holdRepository.findByRequestId(requestId);
            if(maybe.isPresent() && "ACTIVE".equalsIgnoreCase(maybe.get().getStatus().toString())) {
                return HoldDTO.builder()
                    .success(true)
                    .created(false)
                    .holdId(maybe.get().getId())
                    .expiresAt(maybe.get().getExpiresAt())
                    .message("existing active hold (concurrent)")
                    .build();
            }
            // unexpected -> rethrow
            throw ex;
        }

        return HoldDTO.builder()
            .success(true)
            .created(true)
            .holdId(hold.getId())
            .expiresAt(hold.getExpiresAt())
            .message("hold created")
            .build();

    }

    // private holder for inventory records per selection
    private static class SelectedInventoryRecords {
        ReservationItemDTO selection;
        List<InventoryRecord> records;
        SelectedInventoryRecords(ReservationItemDTO selection, List<InventoryRecord> records) { this.selection = selection; this.records = records; }
    }

}
