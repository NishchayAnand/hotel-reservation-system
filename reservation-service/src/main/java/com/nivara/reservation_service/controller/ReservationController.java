package com.nivara.reservation_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nivara.reservation_service.model.dto.InitiateReservationRequestDTO;
import com.nivara.reservation_service.model.dto.InitiateReservationResponseDTO;
import com.nivara.reservation_service.model.entity.Reservation;
import com.nivara.reservation_service.service.ReservationService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/reservations")
@AllArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    

    @PostMapping("/finalize")
    public String finalizeReservation() {
        return null;
    }

}
