package com.nivara.reservation_service.model.dto;

import java.time.LocalDate;
import java.util.List;

public record CreateHoldRequest(
    Long reservationId,
    Long hotelId,
    LocalDate checkInDate,
    LocalDate checkOutDate,
    List<ReservationItemDTO> reservationItems
) {}
