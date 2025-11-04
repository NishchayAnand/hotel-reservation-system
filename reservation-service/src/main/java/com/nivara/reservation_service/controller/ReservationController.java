package com.nivara.reservation_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nivara.reservation_service.model.dto.CreateReservationRequest;
import com.nivara.reservation_service.model.dto.CreateReservationResponse;
import com.nivara.reservation_service.model.entity.Reservation;
import com.nivara.reservation_service.service.ReservationService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/reservations")
@AllArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<CreateReservationResponse> createReservation(
        @RequestHeader(value = "X-Request-ID") String requestId,
        @RequestBody CreateReservationRequest requestBody
    ) {
        Reservation reservation = reservationService.createReservation(
            requestId, 
            requestBody.hotelId(),
            requestBody.checkInDate(),
            requestBody.checkOutDate(),
            requestBody.reservationItems(),
            requestBody.subtotal(),
            requestBody.taxes(),
            requestBody.total(),
            requestBody.currency());

        return ResponseEntity.status(201).body(CreateReservationResponse.from(reservation));
    }

}
