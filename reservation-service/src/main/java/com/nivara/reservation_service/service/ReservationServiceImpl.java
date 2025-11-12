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
import com.nivara.reservation_service.client.PaymentClient;
import com.nivara.reservation_service.exception.HoldDuplicateException;
import com.nivara.reservation_service.exception.HoldExpiredException;
import com.nivara.reservation_service.exception.HoldReleasedException;
import com.nivara.reservation_service.exception.InventoryUnavailableException;
import com.nivara.reservation_service.exception.RemoteServerException;
import com.nivara.reservation_service.model.dto.CreateHoldRequestDTO;
import com.nivara.reservation_service.model.dto.CreateHoldResponseDTO;
import com.nivara.reservation_service.model.dto.CreateOrderRequestDTO;
import com.nivara.reservation_service.model.dto.CreateOrderResponseDTO;
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
    private final PaymentClient paymentClient;

    @Override
    public Reservation createReservation(
        String requestId, 
        Long hotelId, 
        LocalDate checkInDate,
        LocalDate checkOutDate,
        List<ReservationItemDTO> reservationItems,
        Long subtotal,
        Long taxes,
        Long total,
        String currency)
    {

        // Step 1. Perist reservation row with status = PENDING
        Reservation reservation = Reservation.builder()
            .requestId(requestId)
            .hotelId(hotelId)
            .checkInDate(checkInDate)
            .checkOutDate(checkOutDate)
            .status(ReservationStatus.PENDING)
            .createdAt(Instant.now())
            .updatedAt(Instant.now())
            .build();

        List<ReservationItem> items = reservationItems.stream()
            .map(item -> ReservationItem.builder()
                .roomTypeId(item.roomTypeId())
                .qty(item.qty())
                .rate(item.rate())
                .reservation(reservation)
                .build()
            )
            .collect(Collectors.toList());

        reservation.setReservationItems(items);

        try {
            // Spring Data JPA executes this inside a transactional boundary.
            reservationRepository.save(reservation);
        } catch (DataIntegrityViolationException ex) {
            // Idempotency Check: If a reservation already exists with this requestId, return it
            Optional<Reservation> existing = reservationRepository.findByRequestId(requestId);
            if(existing.isPresent()) {
                return existing.get();
            }
        }
        
        // Step 2. Call Inventory Service to create Hold
        CreateHoldRequestDTO holdRequest = new CreateHoldRequestDTO(
            reservation.getId(),
            hotelId,
            checkInDate,
            checkOutDate,
            reservationItems
        );

        CreateHoldResponseDTO holdResponse;
        try {
            // this method will be retried on RemoteServerException
            holdResponse = createInventoryHold(holdRequest);
            // if hold status = HELD and hold is already expired
            if ( "HELD".equalsIgnoreCase(holdResponse.status().toString()) && holdResponse.expiresAt().isBefore(Instant.now()) ) {
                reservation.setStatus(ReservationStatus.HOLD_EXPIRED);
                reservation.setUpdatedAt(Instant.now());
                reservationRepository.save(reservation);

                log.info("Hold expired for reservation {} (holdId={})", reservation.getId(), holdResponse.holdId());
                throw new HoldExpiredException("Hold expired for reservation: " + reservation.getId());
            
                // if hold status = CONFIRMED
            } else if ("CONFIRMED".equalsIgnoreCase(holdResponse.status().toString())) {
                Optional<Reservation> existing = reservationRepository.findByRequestId(requestId);
                if(existing.isPresent()) return existing.get();
                // if not existing, then do what and can this even happen?
            }
            
        } catch (HoldDuplicateException hde) {
            holdResponse = inventoryClient.getHoldByReservationId(reservation.getId());

        } catch (HoldReleasedException hise) {
            reservation.setStatus(ReservationStatus.HOLD_EXPIRED);
            reservation.setUpdatedAt(Instant.now());
            reservationRepository.save(reservation);

            log.info("Hold expired for reservation {} (holdId={})", reservation.getId(), holdResponse.holdId());
            throw new HoldExpiredException("Hold expired for reservation: " + reservation.getId());

        } catch (InventoryUnavailableException iue) {
            // non-retryable: mark reservation FAILED and propagate (map to 4xx at controller)
            reservation.setStatus(ReservationStatus.FAILED);
            reservation.setUpdatedAt(Instant.now());
            reservationRepository.save(reservation);
            log.info("Inventory unavailable for reservation {}: {}", reservation.getId(), iue.getMessage());
            throw iue;

        } catch (RemoteServerException rse) {
            // retryable: Resilience4j will have retried; when it bubbles here it means retries were exhausted mark as transient failure
            reservation.setStatus(ReservationStatus.FAILED);
            reservation.setUpdatedAt(Instant.now());
            reservationRepository.save(reservation);
            log.error("Inventory service transient failure for reservation {}, retries exhausted: {}", reservation.getId(), rse.getMessage());
            throw rse;

        }
        
        // Step 3. Persist Hold info in reservation and set status = PAYMENT_AWAITING
        reservation.setHoldId(holdResponse.holdId());
        reservation.setExpiresAt(holdResponse.expiresAt());
        reservation.setStatus(ReservationStatus.AWAITING_PAYMENT);
        reservationRepository.save(reservation);

        // (continue from here)

        // Step 4. Call payment service to create Payment Order
        CreateOrderRequestDTO order = new CreateOrderRequestDTO(
            holdResponse.hotelId(),
            holdResp.lockedAmount(),
            currency
        );

        CreateOrderResponseDTO orderResp;
        try {
            orderResp = paymentClient.createOrder(requestId, order);
        } catch (Exception e) {
            // compensation & scheduling
            throw new RuntimeException("Failed to create payment order", e);
        }

        reservation.setPaymentOrderId(orderResp.orderId());
        reservation.setStatus(ReservationStatus.AWAITING_PAYMENT);
        reservationRepository.save(reservation);

        return reservation;

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

}
