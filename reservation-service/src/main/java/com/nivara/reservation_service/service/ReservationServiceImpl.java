package com.nivara.reservation_service.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.nivara.reservation_service.client.InventoryServiceClient;
import com.nivara.reservation_service.model.dto.CreateHoldRequest;
import com.nivara.reservation_service.model.dto.CreateHoldResponse;
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

    private final ReservationRepository reservationRepository;
    private final InventoryServiceClient inventoryServiceClient;

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
            throw new RuntimeException("Failed to create inventory hold", e);
        }





        
        return null;

    }

}
