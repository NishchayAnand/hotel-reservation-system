package com.nivara.payment_service.model.dto;

import com.nivara.payment_service.model.enums.ReservationStatus;

public record ConfirmReservationResponseDTO(
    Long reservationId,
    ReservationStatus status,
    Long paymentId
) {

    public boolean isSuccess() {
        return status != null && status == ReservationStatus.CONFIRMED;
    }
}
