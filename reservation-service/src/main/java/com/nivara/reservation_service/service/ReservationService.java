package com.nivara.reservation_service.service;

import com.nivara.reservation_service.model.Reservation;

public interface ReservationService {
    public Reservation findByIdempotencyKey(String idempotencyKey);
}
