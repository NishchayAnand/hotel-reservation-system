package com.nivara.reservation_service.model.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class InitiateReservationRequestDTO {
    private final Long hotelId;
    private final LocalDate checkInDate;
    private final LocalDate checkOutDate;
    private final List<ReservationItemDTO> reservationItems;
}
