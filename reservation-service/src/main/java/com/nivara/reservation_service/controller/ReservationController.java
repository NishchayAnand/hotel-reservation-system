package com.nivara.reservation_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nivara.reservation_service.dto.InitiateReservationRequest;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    @PostMapping("/initiate")
    public ResponseEntity<String> initiateReservation(@RequestBody InitiateReservationRequest req) {
        return null;
    }

    @PostMapping("/finalize")
    public String finalizeReservation() {
        return null;
    }

}
