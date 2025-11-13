package com.nivara.reservation_service.model.dto;

import java.time.Instant;

import com.nivara.reservation_service.model.entity.Reservation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class CreateReservationResponseDTO {
    private Long reservationId;
    private String status; // required for controller to return the proper HTTP status code.
    private Long holdId;
    private Instant expiresAt;

    public static CreateReservationResponseDTO from(Reservation reservation) {
        return new CreateReservationResponseDTO(
            reservation.getId(),
            reservation.getStatus().toString(),
            reservation.getHoldId(),
            reservation.getExpiresAt()
        );
    }
}
