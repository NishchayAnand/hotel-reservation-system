package com.nivara.reservation_service.service;

import org.springframework.stereotype.Service;

import com.nivara.reservation_service.model.Reservation;

@Service
public class ReservationServiceImpl implements ReservationService {

    @Override
    public Reservation findByIdempotencyKey(String idempotencyKey) {
        return null;
    }

}
