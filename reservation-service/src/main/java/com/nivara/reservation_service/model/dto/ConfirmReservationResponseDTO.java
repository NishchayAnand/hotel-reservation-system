package com.nivara.reservation_service.model.dto;

import com.nivara.reservation_service.model.enums.ReservationStatus;

public record ConfirmReservationResponseDTO(
    Long reservationId,
    ReservationStatus status,
    Long paymentId
) {}
