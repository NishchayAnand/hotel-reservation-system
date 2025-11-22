package com.nivara.payment_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "reservation-service", url = "http://localhost:8084/api/reservations")
public interface ReservationServiceClient {

    @PostMapping("/{reservationId}/confirm")
    void confirmReservation(
        @PathVariable Long reservationId,
        @RequestParam Long paymentId
    );

}
