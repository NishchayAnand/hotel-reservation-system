package com.nivara.reservation_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.nivara.reservation_service.model.dto.CreatePaymentOrderRequestDTO;
import com.nivara.reservation_service.model.dto.CreatePaymentOrderResponseDTO;

@FeignClient(name = "payment-service", url = "http://localhost:8083/api/payments")
public interface PaymentClient {

    @PostMapping("create-order")
    CreatePaymentOrderResponseDTO createPaymentOrder(@RequestBody CreatePaymentOrderRequestDTO requestBody);

}
