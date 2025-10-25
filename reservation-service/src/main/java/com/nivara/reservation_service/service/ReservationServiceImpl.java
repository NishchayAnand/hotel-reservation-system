package com.nivara.reservation_service.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.nivara.reservation_service.model.dto.ReservationItemDTO;
import com.nivara.reservation_service.model.entity.Reservation;

@Service
public class ReservationServiceImpl implements ReservationService {

    @Override
    public Reservation exists(String idempotencyKey) {
        return null;
    }

    @Override
    public Reservation initiate(Long hotelId, 
        LocalDate checkInDate, 
        LocalDate checkOutDate, 
        List<ReservationItemDTO> reservationItems) {
        
        return null;

    }

}
