package com.nivara.reservation_service.model.dto;

public record CreateReservationResponse (
    Long reservationId,
    String status,
    Long holdId,
    String orderId,
    String expiresAt
) {}
