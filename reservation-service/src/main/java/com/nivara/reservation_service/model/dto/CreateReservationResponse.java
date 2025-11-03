package com.nivara.reservation_service.model.dto;

import java.time.Instant;

import com.nivara.reservation_service.model.entity.Reservation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class CreateReservationResponse {
    private Long reservationId;
    private String status; // required for controller to return the proper HTTP status code.
    private Long holdId;
    private Instant expiresAt;
    private String paymentOrderId;
    //private String checkOutUrl; // to decouple frontend from the payment service provider

    public static CreateReservationResponse from(Reservation reservation) {
        return new CreateReservationResponse(
            reservation.getId(),
            reservation.getStatus().toString(),
            reservation.getHoldId(),
            reservation.getExpiresAt(),
            reservation.getPaymentOrderId()
        );
    }
}
