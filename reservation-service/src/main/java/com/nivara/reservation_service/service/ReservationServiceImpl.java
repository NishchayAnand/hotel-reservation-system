package com.nivara.reservation_service.service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.nivara.reservation_service.client.InventoryClient;
import com.nivara.reservation_service.exception.HoldDuplicateException;
import com.nivara.reservation_service.exception.HoldExpiredException;
import com.nivara.reservation_service.exception.HoldReleasedException;
import com.nivara.reservation_service.exception.InventoryUnavailableException;
import com.nivara.reservation_service.exception.RemoteServerException;
import com.nivara.reservation_service.model.dto.CreateHoldRequestDTO;
import com.nivara.reservation_service.model.dto.CreateHoldResponseDTO;
import com.nivara.reservation_service.model.dto.ReservationItemDTO;
import com.nivara.reservation_service.model.entity.Reservation;
import com.nivara.reservation_service.model.entity.ReservationItem;
import com.nivara.reservation_service.model.enums.ReservationStatus;
import com.nivara.reservation_service.repository.ReservationRepository;

import io.github.resilience4j.retry.annotation.Retry;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private static final Logger log = LoggerFactory.getLogger(ReservationService.class);

    private final ReservationRepository reservationRepository;
    private final InventoryClient inventoryClient;

    @Override
    public Reservation createReservation(
        String requestId, 
        Long hotelId, 
        LocalDate checkInDate,
        LocalDate checkOutDate,
        List<ReservationItemDTO> reservationItems,
        Long amount,
        String currency)
    {

        // Step 1. Perist reservation row with status = PENDING
        Reservation reservation = Reservation.builder()
            .requestId(requestId)
            .hotelId(hotelId)
            .checkInDate(checkInDate)
            .checkOutDate(checkOutDate)
            .amount(amount)
            .currency(currency)
            .status(ReservationStatus.PENDING)
            .createdAt(Instant.now())
            .updatedAt(Instant.now())
            .build();

        List<ReservationItem> items = reservationItems.stream()
            .map(item -> ReservationItem.builder()
                .roomTypeId(item.roomTypeId())
                .quantity(item.quantity())
                .rate(item.rate())
                .reservation(reservation)
                .build()
            )
            .collect(Collectors.toList());

        reservation.setReservationItems(items);

        Reservation saved = null;
        try {
            // Spring Data JPA executes this inside a transactional boundary.
            saved = reservationRepository.save(reservation);
        } catch (DataIntegrityViolationException ex) {
            // Idempotency Check: If a reservation already exists with this requestId, return it
            Optional<Reservation> existing = reservationRepository.findByRequestId(requestId);
            if(existing.isPresent()) {
                return existing.get();
            }
        }

        if(saved == null) {
            throw new RuntimeException("Failed to create reservation: unknown");
        }
        
        // Step 2. Call Inventory Service to create Hold
        CreateHoldRequestDTO holdRequest = new CreateHoldRequestDTO(
            saved.getId(), 
            hotelId, 
            checkInDate, 
            checkOutDate, 
            reservationItems
        );

        CreateHoldResponseDTO holdResponse;
        try {
            // this method will be retried on RemoteServerException
            holdResponse = createInventoryHold(holdRequest);
            
        } catch (HoldDuplicateException hde) {
            holdResponse = inventoryClient.getHoldByReservationId(reservation.getId());

        } catch (HoldReleasedException hise) {
            saved.setStatus(ReservationStatus.HOLD_EXPIRED);
            saved.setUpdatedAt(Instant.now());
            reservationRepository.save(saved);

            log.info("Hold expired for reservation: {}", reservation.getId());
            throw new HoldExpiredException("Hold expired for reservation: " + reservation.getId());

        } catch (InventoryUnavailableException iue) {
            // non-retryable: mark reservation FAILED and propagate (map to 4xx at controller)
            saved.setStatus(ReservationStatus.FAILED);
            saved.setUpdatedAt(Instant.now());
            reservationRepository.save(saved);
            log.info("Inventory unavailable for reservation {}: {}", reservation.getId(), iue.getMessage());
            throw iue;

        } catch (RemoteServerException rse) {
            // retryable: Resilience4j will have retried; when it bubbles here it means retries were exhausted mark as transient failure
            saved.setStatus(ReservationStatus.FAILED);
            saved.setUpdatedAt(Instant.now());
            reservationRepository.save(saved);
            log.error("Inventory service transient failure for reservation {}, retries exhausted: {}", reservation.getId(), rse.getMessage());
            throw rse;

        }

        // if hold status = HELD and hold is already expired
        if ( "HELD".equalsIgnoreCase(holdResponse.status().toString()) && holdResponse.expiresAt().isBefore(Instant.now()) ) {
            saved.setStatus(ReservationStatus.HOLD_EXPIRED);
            saved.setUpdatedAt(Instant.now());
            reservationRepository.save(saved);

            log.info("Hold expired for reservation {} (holdId={})", reservation.getId(), holdResponse.holdId());
            throw new HoldExpiredException("Hold expired for reservation: " + reservation.getId());
        
            // if hold status = CONFIRMED
        } else if ("CONFIRMED".equalsIgnoreCase(holdResponse.status().toString())) {
            Optional<Reservation> existing = reservationRepository.findByRequestId(requestId);
            if(existing.isPresent()) return existing.get();
        }
        
        // Step 3. Persist Hold info in reservation and set status = PAYMENT_AWAITING
        saved.setHoldId(holdResponse.holdId());
        saved.setExpiresAt(holdResponse.expiresAt());
        saved.setStatus(ReservationStatus.AWAITING_PAYMENT);
        reservationRepository.save(saved);

        return saved;

    }

    @Retry(name = "createInventoryHoldRetry", fallbackMethod = "createInventoryHoldFallback")
    private CreateHoldResponseDTO createInventoryHold(CreateHoldRequestDTO holdReq) {
        return inventoryClient.createHold(holdReq);
    }

    @SuppressWarnings("unused")
    private CreateHoldResponseDTO createInventoryHoldFallback(CreateHoldRequestDTO req, Throwable ex) {
        reservationRepository.findById(req.reservationId()).ifPresent(reservation -> {
            reservation.setStatus(ReservationStatus.FAILED);
            reservation.setUpdatedAt(Instant.now());
            reservationRepository.save(reservation);
        });
        log.error("createHold failed for reservationId={}", req.reservationId(), ex);
        throw new RuntimeException("Failed to create inventory hold. Inventory service error: ", ex);
    }

    @Override
    public Reservation findById(Long reservationId) {
        return reservationRepository.findById(reservationId).get();
    }

}
