package com.nivara.reservation_service.service;

import com.nivara.reservation_service.model.dto.CreateReservationRequest;
import com.nivara.reservation_service.model.dto.CreateReservationResponse;

public interface ReservationService {
    public CreateReservationResponse createReservation(String requestId, CreateReservationRequest requestBody);
}
