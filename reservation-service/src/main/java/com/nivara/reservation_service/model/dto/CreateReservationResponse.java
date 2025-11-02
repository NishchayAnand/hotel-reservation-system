package com.nivara.reservation_service.model.dto;

import java.time.Instant;

public record CreateReservationResponse (
    Long reservationId,
    String status,
    Long holdId,
    String orderId,
    Instant expiresAt
) {}
