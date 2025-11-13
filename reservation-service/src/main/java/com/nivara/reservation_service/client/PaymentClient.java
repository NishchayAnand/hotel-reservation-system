package com.nivara.reservation_service.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "payment-service", url = "http://localhost:8083/api/payments")
public interface PaymentClient {}
