package com.nivara.reservation_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PostMapping("/initiate")
    public ResponseEntity<InitiateReservationResponseDTO> initiateReservation(@RequestBody InitiateReservationRequestDTO req) {
        // idempotent lookup
        Reservation existing = reservationService.findByIdempotencyKey(req.getIdempotencyKey());
        if(existing != null) {
            var resp = InitiateReservationResponseDTO.builder()
                .reservationId(existing.getId())
                .status(existing.getStatus())
                .message("Duplicate request")
                .build();
            return ResponseEntity.status(409).body(resp);
        }
        //Reservation r = reservationService.initiate(req);
        return null;
    }

    @PostMapping("/finalize")
    public String finalizeReservation() {
        return null;
    }

}
