package com.nivara.reservation_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nivara.reservation_service.mapper.ReservationMapper;
import com.nivara.reservation_service.model.dto.ConfirmReservationResponseDTO;
import com.nivara.reservation_service.model.dto.CreateReservationRequestDTO;
import com.nivara.reservation_service.model.dto.CreateReservationResponseDTO;
import com.nivara.reservation_service.model.dto.ReservationDTO;
import com.nivara.reservation_service.model.entity.Reservation;
import com.nivara.reservation_service.service.ReservationService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/reservations")
@AllArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<CreateReservationResponseDTO> createReservation(
        @RequestHeader(value = "X-Request-Id") String requestId,
        @RequestBody CreateReservationRequestDTO requestBody
    ) {
        Reservation reservation = reservationService.createReservation(
            requestId, 
            requestBody.hotelId(),
            requestBody.checkInDate(),
            requestBody.checkOutDate(),
            requestBody.reservationItems(),
            requestBody.amount(),
            requestBody.currency());

        return ResponseEntity.status(201).body(CreateReservationResponseDTO.from(reservation));
    }

    @GetMapping("/{reservationId}")
    public ResponseEntity<ReservationDTO> getReservation(@PathVariable Long reservationId) {
        Reservation reservation = reservationService.findById(reservationId);
        ReservationDTO resp = ReservationMapper.toDto(reservation);
        return ResponseEntity.ok().body(resp);
    }

    @PostMapping("/{reservationId}/confirm")
    public ResponseEntity<ConfirmReservationResponseDTO> confirmReservation(@PathVariable Long reservationId, @RequestParam Long paymentId) {
        ConfirmReservationResponseDTO responseBody = reservationService.confirmReservation(reservationId, paymentId);
        return ResponseEntity.ok().body(responseBody);
    }

}
