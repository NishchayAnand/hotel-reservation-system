package com.nivara.reservation_service.model.dto;

public record ConfirmReservationResponseDTO(
    Long reservationId,
    String status,
    Long paymentId
) {}
