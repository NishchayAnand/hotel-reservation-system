package com.nivara.reservation_service.model.dto;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import com.nivara.reservation_service.model.enums.ReservationStatus;

public record ReservationDTO (
    Long id,
    Long hotelId,
    LocalDate checkInDate,
    LocalDate checkOutDate,
    List<ReservationItemDTO> reservedItems,
    Long amount,
    String currency,
    Long holdId,
    Instant expiresAt,
    ReservationStatus status,
    Instant createdAt,
    Instant updatedAt
) {}
