package com.nivara.payment_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.nivara.payment_service.model.dto.ConfirmReservationResponseDTO;

@FeignClient(name = "reservation-service", url = "${service.reservation-service.base-url}/api/reservations")
public interface ReservationServiceClient {

    @PostMapping("/{reservationId}/confirm")
    ConfirmReservationResponseDTO confirmReservation(@PathVariable Long reservationId, @RequestParam Long paymentId);

}
