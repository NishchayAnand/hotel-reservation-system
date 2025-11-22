package com.gharana.inventory_service.service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.gharana.inventory_service.model.entity.InventoryRecord;
import com.gharana.inventory_service.model.enums.HoldStatus;
import com.gharana.inventory_service.exception.HoldNotFoundException;
import com.gharana.inventory_service.exception.HoldReleasedException;
import com.gharana.inventory_service.exception.InventoryUnavailableException;
import com.gharana.inventory_service.model.dto.AvailableRoomTypeDTO;
import com.gharana.inventory_service.model.dto.ConsumeHoldResponseDTO;
import com.gharana.inventory_service.model.dto.HoldDTO;
import com.gharana.inventory_service.model.dto.HoldNotConsumableException;
import com.gharana.inventory_service.model.dto.ReleaseHoldResponseDTO;
import com.gharana.inventory_service.model.dto.ReservationItemDTO;
import com.gharana.inventory_service.model.entity.Hold;
import com.gharana.inventory_service.model.entity.HoldItem;
import com.gharana.inventory_service.repository.HoldRepository;
import com.gharana.inventory_service.repository.InventoryRepository;

import jakarta.transaction.Transactional;

@Service
public class InventoryServiceImpl implements InventoryService {

    private static final Logger log = LoggerFactory.getLogger(InventoryServiceImpl.class);

    private InventoryRepository inventoryRepository;
    private HoldRepository holdRepository;

    private final long defaultHoldTtlSeconds;

    // Constructor
    public InventoryServiceImpl(InventoryRepository inventoryRepository, 
                                HoldRepository holdRepository, 
                                @Value("${inventory.hold.ttl-seconds:900}") long defaultHoldTtlSeconds) {
        this.inventoryRepository = inventoryRepository;
        this.holdRepository = holdRepository;
        this.defaultHoldTtlSeconds = defaultHoldTtlSeconds;
    }

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
        List<ReservationItemDTO> reservationItems
    ) {

        // Step 1: Idempotent Check: If hold already exists for this reservation, return or error depending on status
        Optional<Hold> existing = holdRepository.findByReservationId(reservationId);
        if(existing.isPresent()) {
            Hold existingHold = existing.get();
            String status = existingHold.getStatus().toString();
            if ("HELD".equals(status) || "CONFIRMED".equals(status)) {
                // idempotent success: return existing hold
                log.info("Returning existing hold for reservationId={} status={}", reservationId, status);
                return existingHold;
            } else {
                // RELEASED -> cannot reuse; ask caller to create a new reservation request.
                log.warn("Attempt to recreate hold for reservationId={} which {}. Ignoring request.", reservationId, status);
                throw new HoldReleasedException("Hold already exists and cannot be reused.");
            }
        }

        // Step 2: For each reservation item (roomType), lock inventory for the date range and validate inventory 
        // Keep the locked rows per roomType so we can decrement and pesist later
        Map<Long, List<InventoryRecord>> lockedInventoryRecordsByRoomType = new HashMap<>();

        for(ReservationItemDTO item: reservationItems) {
            Long roomTypeId = item.roomTypeId();
            int quantity = item.quantity();

            // This method must be annotated with @Lock(PESSIMISTIC_WRITE) to lock the selected roomType inventory for the [checkInDate, checkOutDate).
            List<InventoryRecord> inventoryRecords = inventoryRepository.findForUpdateBetweenDates(hotelId, roomTypeId, checkInDate, checkOutDate.minusDays(1));

            // Verify per-day availability.
            for(InventoryRecord inventoryRecord: inventoryRecords) {
                int availableQuantity = inventoryRecord.getTotalCount() - inventoryRecord.getReservedCount();
                if(availableQuantity < quantity) {
                    log.debug("Insufficient inventory for hotel={}, roomType={}, date={}, available={}, requested={}",
                        hotelId, roomTypeId, inventoryRecord.getReservationDate(), availableQuantity, quantity);
                    throw new InventoryUnavailableException("Insufficient inventory for date " + inventoryRecord.getReservationDate());
                }
            }

            // All good for this roomType, decrement in-memory now
            for(InventoryRecord inventoryRecord: inventoryRecords) {
                inventoryRecord.setReservedCount(inventoryRecord.getReservedCount() + item.quantity());
            }

            lockedInventoryRecordsByRoomType.put(roomTypeId, inventoryRecords);
        
        }

        // Step 3: Persist updated inventory rows (still inside the same transaction)
        List<InventoryRecord> allInventoryRecordsToSave = lockedInventoryRecordsByRoomType.values().stream() // returns Stream<Collecton<InventoryRecord>>
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
        inventoryRepository.saveAll(allInventoryRecordsToSave);

        // Step 4: Create Hold and HoldItems, perist hold (in same transaction)
        Instant expiresAt = Instant.now().plusSeconds(defaultHoldTtlSeconds);

        Hold hold = Hold.builder()
            .reservationId(reservationId)
            .hotelId(hotelId)
            .checkInDate(checkInDate)
            .checkOutDate(checkOutDate)
            .status(HoldStatus.ACTIVE)
            .expiresAt(expiresAt)
            .build();
        
        List<HoldItem> heldItems = new ArrayList<>();
        for(ReservationItemDTO reservationItem: reservationItems) {
            HoldItem heldItem = HoldItem.builder()
                .hold(hold)
                .roomTypeId(reservationItem.roomTypeId())
                .quantity(reservationItem.quantity())
                .build();
            heldItems.add(heldItem);
        }
        hold.setHeldItems(heldItems);

        // Step 5: Persist the Hold and HoldItems - still inside the transaction
        // Two concurrent requests with the same reservationId (idempotency key) can both pass the availability checks and inventory increments.
        // The DB unique constraint on reservationId prevents the second insert and throws a DataIntegrityViolationException, leading to transaction roll back.
        Hold saved = null;
        try {
            saved = holdRepository.save(hold);

        } catch(DataIntegrityViolationException ex) {
            // Idempotency Check: If a reservation already exists with this requestId, return it
            Optional<Hold> maybe = holdRepository.findByReservationId(reservationId);
            if(maybe.isPresent()) {
                return maybe.get();
            }
        }

        if(saved == null) {
            throw new RuntimeException("Unknown error occured while persisting hold");
        }
        
        log.info("Created hold {} for reservation {} (expiresAt={})", saved.getId(), reservationId, expiresAt);
        return saved;

    }

    public HoldDTO getHold(Long holdId) {
        Hold hold = holdRepository.findById(holdId).get();
        return new HoldDTO(hold.getId(), hold.getStatus(), hold.getExpiresAt());
    }

    @Override
    @Transactional
    public ConsumeHoldResponseDTO consumeHold(Long holdId, Long reservationId, Long paymentId) {

        // Step 1: Load hold with write lock (protect against concurrent consumes)
        Hold hold = holdRepository.findByIdForUpdate(holdId)
            .orElseThrow(() -> new HoldNotFoundException(holdId));

        // Step 2: Idempotency check: if already CONSUMED for same reservation & payment - return OK
        if(hold.isConsumed()) {
            if( Objects.equals(hold.getReservationId(), reservationId) && 
                Objects.equals(hold.getPaymentId(), paymentId)) {
                
                return new ConsumeHoldResponseDTO(
                    hold.getId(),
                    hold.getStatus(),
                    hold.getReservationId(),
                    hold.getPaymentId()
                );
            }

            // consumed but with different reservation/payment - treat as conflict
            throw new HoldNotConsumableException(
                    "Hold " + holdId + " already consumed for reservation " +
                    hold.getReservationId() + " and payment " + hold.getPaymentId()
            );
        }

        // 3. Validate hold is ACTIVE and not expired
        if (!hold.isActive()) {
            throw new HoldNotConsumableException(
                    "Hold " + holdId + " is not ACTIVE (status=" + hold.getStatus() + ")"
            );
        }

        if (hold.isExpired()) {
            hold.setStatus(HoldStatus.EXPIRED);
            // save status update so future calls see it as EXPIRED
            holdRepository.save(hold);

            throw new HoldNotConsumableException(
                    "Hold " + holdId + " has expired and cannot be consumed"
            );
        }

        // 4. Optional: sanity checks for reservationId/payments
        if (hold.getReservationId() != null
                && !Objects.equals(hold.getReservationId(), reservationId)) {
            throw new HoldNotConsumableException(
                    "Hold " + holdId + " is already linked to reservation " + hold.getReservationId()
            );
        }

        // 5. Link to reservation & payment and mark as CONSUMED
        hold.setReservationId(reservationId);
        hold.setPaymentId(paymentId);
        hold.setStatus(HoldStatus.CONSUMED);

        Hold saved = holdRepository.save(hold);

        // 6. Return result
        return new ConsumeHoldResponseDTO(
                saved.getId(),
                saved.getStatus(),
                saved.getReservationId(),
                saved.getPaymentId()
        );

    }

    @Override
    @Transactional
    public ReleaseHoldResponseDTO releaseHold(ReleaseHoldRequest request) {
        Long holdId = request.holdId();

        // 1. Load hold WITH LOCK
        Hold hold = holdRepository.findByIdForUpdate(holdId)
            .orElseThrow(() -> new IllegalArgumentException("Hold not found: " + holdId));

        // 2. Handle status / idempotency
        if (hold.getStatus() == HoldStatus.RELEASED || hold.getStatus() == HoldStatus.EXPIRED) {
            // idempotent behaviour: nothing to do, just return current state
            return new ReleaseHoldResponse(hold.getId(), hold.getStatus());
        }

        if (hold.getStatus() == HoldStatus.CONSUMED) {
            // rooms already permanently consumed into a reservation;
            // cannot release them back to inventory
            throw new IllegalStateException("Cannot release a consumed hold: " + holdId);
        }

        if (hold.getStatus() != HoldStatus.HELD) {
            throw new IllegalStateException("Unexpected hold status: " + hold.getStatus());
        }

        // 3. Restore inventory for each item in the hold
        LocalDate startDate = hold.getCheckInDate().toLocalDate();
        LocalDate endDateExclusive = hold.getCheckOutDate().toLocalDate();

        for (HoldItem item : hold.getItems()) {
            Long roomTypeId = item.getRoomTypeId();
            int qty = item.getQuantity();

            // We want [startDate, endDateExclusive) so we use endDateExclusive.minusDays(1)
            List<RoomInventory> inventories = roomInventoryRepository.findForUpdate(
                hold.getHotelId(),
                roomTypeId,
                startDate,
                endDateExclusive.minusDays(1)
            );

            if (inventories.size() !=
                (int) startDate.datesUntil(endDateExclusive).count()) {
                throw new IllegalStateException(
                    "Room inventory rows mismatch for holdId " + holdId + ", roomTypeId " + roomTypeId
                );
            }

            // Increment availableRooms back
            for (RoomInventory inv : inventories) {
                inv.setAvailableRooms(inv.getAvailableRooms() + qty);
            }
        }

        // 4. Update hold status
        hold.setStatus(HoldStatus.RELEASED);
        hold.setReleasedAt(OffsetDateTime.now(clock));
        // optionally store reason if you have a field:
        // hold.setReleaseReason(request.reason());

        // 5. Save (flush happens automatically at transaction commit)
        holdRepository.save(hold);

        return new ReleaseHoldResponse(hold.getId(), hold.getStatus());
    }

}
