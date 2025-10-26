package com.gharana.inventory_service.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.gharana.inventory_service.model.entity.InventoryRecord;
import com.gharana.inventory_service.model.enums.HoldStatus;
import com.gharana.inventory_service.model.dto.AvailableRoomTypeDTO;
import com.gharana.inventory_service.model.dto.HoldDTO;
import com.gharana.inventory_service.model.dto.SelectedInventoryDTO;
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

    @Override
    public List<AvailableRoomTypeDTO> getAvailableRoomTypes(Set<Long> hotelIds, LocalDate checkInDate, LocalDate checkOutDate) { 
        // Fetch room types for the given hotels that have availability across the entire [checkInDate, checkOutDate) range.  
        long nights = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        List<Object[]> rows = inventoryRepository.findAvailableRoomTypesForHotels(hotelIds, checkInDate, checkOutDate, nights); 
        return rows.stream()
            .map(row -> new AvailableRoomTypeDTO( // row[0] => hotelId, row[1] => roomTypeId, row[2] => availableRoomCount
                (Long) row[0], 
                (Long) row[1],
                (int) row[2])) 
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public HoldDTO createInventoryHold(String requestId, 
        Long hotelId, 
        LocalDate checkInDate, 
        LocalDate checkOutDate,
        List<SelectedInventoryDTO> selectedInventory) {

        // Step 1: Perform idempotent check to verify if an ACTIVE hold exists for this requestId
        Optional<Hold> existing = holdRepository.findByRequestId(requestId);
        if(existing.isPresent()) {
            Hold hold = existing.get();
            if(hold.getStatus().toString().equalsIgnoreCase("ACTIVE")) {
                return HoldDTO.builder()
                    .holdId(hold.getId())
                    .success(true)
                    .created(false)
                    .expiresAt(hold.getExpiresAt())
                    .message("existing active hold")
                    .build();
            }
            // if exists but not ACTIVE, can attempt new hold, if required.
        }

        // Step 2: Pre-check & lock inventory rows for every selected room type
        List<SelectedInventoryRecords> selectedInventoryRecords = new ArrayList<>();
        for(SelectedInventoryDTO selection: selectedInventory) {
            
            List<InventoryRecord> records = inventoryRepository.findByHotelIdAndRoomTypeIdAndReservationDateBetween(
                hotelId, 
                selection.getRoomTypeId(), 
                checkInDate, 
                checkInDate.minusDays(1));

            // do availability check to ensure the selected quantity is still available
            for(InventoryRecord record: records) {
                int availableRooms = record.getTotalCount() - record.getReservedCount();
                if(availableRooms < selection.getCount()) {
                    return HoldDTO.builder()
                        .success(false)
                        .message("insufficient inventory for roomType:" + selection.getRoomTypeId())
                        .build();
                }
            }

            selectedInventoryRecords.add(new SelectedInventoryRecords(selection, records));
        
        }

        // Step 3: All checks passed -> increment reserved_count for each locked record
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
        for(SelectedInventoryDTO selection: selectedInventory) {
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
        SelectedInventoryDTO selection;
        List<InventoryRecord> records;
        SelectedInventoryRecords(SelectedInventoryDTO selection, List<InventoryRecord> records) { this.selection = selection; this.records = records; }
    }

}
