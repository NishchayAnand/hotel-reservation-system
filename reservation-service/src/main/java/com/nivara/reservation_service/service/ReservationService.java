package com.nivara.reservation_service.service;

import java.time.LocalDate;
import java.util.List;

import com.nivara.reservation_service.model.dto.ReservationItemDTO;
import com.nivara.reservation_service.model.entity.Reservation;

public interface ReservationService {

    public Reservation createReservation(
        String requestId, 
        Long hotelId, 
        LocalDate checkInDate,
        LocalDate checkOutDate,
        List<ReservationItemDTO> reservationItems,
        Long amount,
        String currency
    );

    public Reservation findById(Long reservationId);

}
