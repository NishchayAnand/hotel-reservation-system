package com.nivara.reservation_service.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class InitiateReservationRequest {
    private final Long hotelId;
    private final LocalDate checkInDate;
    private final LocalDate checkOutDate;
    private final List<ReservationItem> reservationItems;
    private final long total;
    private final String paymentMethod;
}
