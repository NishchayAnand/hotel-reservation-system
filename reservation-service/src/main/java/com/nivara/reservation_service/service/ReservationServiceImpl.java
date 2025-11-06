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
import com.nivara.reservation_service.model.dto.CreateHoldRequest;
import com.nivara.reservation_service.model.dto.CreateHoldResponse;
import com.nivara.reservation_service.model.dto.CreateOrderRequest;
import com.nivara.reservation_service.model.dto.CreateOrderResponse;
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
            reservationRepository.save(reservation); // Spring Data JPA executes this inside a transactional boundary.
        } catch (DataIntegrityViolationException ex) {
            // Idempotency Check: If a reservation already exists with this requestId, return it
            Optional<Reservation> existing = reservationRepository.findByRequestId(requestId);
            if(existing.isPresent()) {
                return existing.get();
            }
        }
        
        // Step 2. Call Inventory Service to create Hold
        CreateHoldRequest holdReq = new CreateHoldRequest(
            reservation.getId(),
            hotelId,
            checkInDate,
            checkOutDate,
            reservationItems
        );

        CreateHoldResponse holdResp = createInventoryHold(holdReq);

        // Step 3. Persist Hold info in reservation and set status = PAYMENT_AWAITING
        reservation.setHoldId(holdResp.holdId());
        reservation.setExpiresAt(holdResp.expiresAt());
        reservation.setStatus(ReservationStatus.AWAITING_PAYMENT);
        try {
            reservationRepository.save(reservation);
            log.info("Hold created {} for reservation {}", holdResp.holdId(), reservation.getId());
        } catch (Exception ex) {
            // schedule compensation / retry; log
        }

        // Step 4. Call payment service to create Payment Order
        CreateOrderRequest order = new CreateOrderRequest(
            holdReq.hotelId(),
            holdResp.lockedAmount(),
            currency
        );

        CreateOrderResponse orderResp;
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
    private CreateHoldResponse createInventoryHold(CreateHoldRequest holdReq) {
        return inventoryClient.createHold(holdReq);
    }

    @SuppressWarnings("unused")
    private CreateHoldResponse createInventoryHoldFallback(CreateHoldRequest req, Throwable ex) {
        reservationRepository.findById(req.reservationId()).ifPresent(reservation -> {
            reservation.setStatus(ReservationStatus.FAILED);
            reservation.setUpdatedAt(Instant.now());
        });
        log.error("createHold failed for reservationId={}", req.reservationId(), ex);
        throw new RuntimeException("Failed to create inventory hold: ", ex);
    }

}
