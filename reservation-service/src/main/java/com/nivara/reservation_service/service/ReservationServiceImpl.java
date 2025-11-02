package com.nivara.reservation_service.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.nivara.reservation_service.client.InventoryServiceClient;
import com.nivara.reservation_service.client.PaymentServiceClient;
import com.nivara.reservation_service.model.dto.CreateHoldRequest;
import com.nivara.reservation_service.model.dto.CreateHoldResponse;
import com.nivara.reservation_service.model.dto.CreateOrderRequest;
import com.nivara.reservation_service.model.dto.CreateOrderResponse;
import com.nivara.reservation_service.model.dto.CreateReservationRequest;
import com.nivara.reservation_service.model.dto.CreateReservationResponse;
import com.nivara.reservation_service.model.entity.Reservation;
import com.nivara.reservation_service.model.enums.ReservationStatus;
import com.nivara.reservation_service.repository.ReservationRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private static final Logger log = LoggerFactory.getLogger(ReservationService.class);

    private final ReservationRepository reservationRepository;
    private final InventoryServiceClient inventoryServiceClient;
    private final PaymentServiceClient paymentServiceClient;

    @Override
    @Transactional
    public CreateReservationResponse createReservation(String requestId, CreateReservationRequest requestBody) {

        // 1. Idempotency Check: If a reservation already exists with this requestId, return it
        Optional<Reservation> existing = reservationRepository.findByRequestId(requestId);
        if(existing.isPresent()) {
            Reservation reservation = existing.get();
            return new CreateReservationResponse(
                reservation.getId(), 
                reservation.getStatus().toString(),
                reservation.getHoldId(),
                reservation.getOrderId(),
                reservation.getExpiresAt()
            );
        } 

        // 2. Perist reservation
        Reservation reservation = Reservation.builder()
            .requestId(requestId)
            .hotelId(requestBody.hotelId())
            .checkInDate(requestBody.checkInDate())
            .checkOutDate(requestBody.checkOutDate())
            .status(ReservationStatus.INIT)
            .createdAt("")
            .build();
        reservationRepository.save(reservation);

        // 3. Call inventory-service to create hold
        CreateHoldRequest holdReq = new CreateHoldRequest(
            requestBody.hotelId(),
            requestBody.checkInDate(),
            requestBody.checkOutDate(),
            requestBody.reservationItems()
        );

        CreateHoldResponse holdResp;
        try {
            holdResp = inventoryServiceClient.createHold(requestId, holdReq);  
        } catch (Exception e) {
            log.error("inventory.createHold failed for reservationId={}", reservation.getId(), e);
            reservation.setStatus(ReservationStatus.FAILED);
            reservationRepository.save(reservation);
            throw new RuntimeException("Failed to create inventory hold: ", e);
        }

        // 4. Persist Hold info and set HOLD_CREATED
        reservation.setHoldId(holdResp.holdId());
        reservation.setExpiresAt(holdResp.expiresAt());
        reservation.setStatus(ReservationStatus.HOLD_CREATED);
        reservationRepository.save(reservation);
        log.info("Hold created {} for reservation {}", holdResp.holdId(), reservation.getId());

        // 5. Call payment-service to create Payment Order
        CreateOrderRequest order = new CreateOrderRequest(
            holdReq.hotelId(),
            holdResp.lockedAmount(),
            requestBody.currency()
        );

        CreateOrderResponse orderResp;
        try {
            orderResp = paymentServiceClient.createOrder(requestId, order);
        } catch (Exception e) {
            // compensation & scheduling
            throw new RuntimeException("Failed to create payment order", e);
        }

        reservation.setOrderId(orderResp.orderId());
        reservation.setStatus(ReservationStatus.ORDER_CREATED);
        reservationRepository.save(reservation);

        return new CreateReservationResponse(
            reservation.getId(),
            reservation.getStatus().toString(),
            reservation.getHoldId(),
            reservation.getOrderId(),
            reservation.getExpiresAt()
        );

    }

}
