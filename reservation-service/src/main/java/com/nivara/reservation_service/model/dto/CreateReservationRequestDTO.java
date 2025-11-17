package com.nivara.reservation_service.model.dto;

import java.time.LocalDate;
import java.util.List;

public record CreateReservationRequestDTO(
    Long hotelId,
    LocalDate checkInDate,
    LocalDate checkOutDate,
    List<ReservationItemDTO> reservationItems,
    Long amount,
    String currency
) {}
