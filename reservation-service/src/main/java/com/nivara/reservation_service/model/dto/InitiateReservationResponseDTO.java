package com.nivara.reservation_service.model.dto;

import com.nivara.reservation_service.model.enums.ReservationStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InitiateReservationResponseDTO {
    private Long reservationId;
    private ReservationStatus status;
    private String message;
}
