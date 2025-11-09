package com.nivara.reservation_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.nivara.reservation_service.model.dto.CreateOrderRequestDTO;
import com.nivara.reservation_service.model.dto.CreateOrderResponseDTO;

@FeignClient(name = "payment-service", url = "http://localhost:8083/api/payments")
public interface PaymentClient {

    @PostMapping("create-order")
    CreateOrderResponseDTO createOrder(
        @RequestHeader("X-Request-ID") String requestId,
        @RequestBody CreateOrderRequestDTO requestBody
    );

}
