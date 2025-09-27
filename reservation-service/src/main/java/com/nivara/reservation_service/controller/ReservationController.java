package com.nivara.reservation_service.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    @PostMapping("/initiate")
    public String initiateReservation() {
        return null;
    }

    @PostMapping("/finalize")
    public String finalizeReservation() {
        return null;
    }

}
