package com.nivara.payment_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nivara.payment_service.model.dto.PaymentRequestDTO;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @PostMapping("")
    public ResponseEntity<String> processPayment(
        @RequestHeader(value = "X-Request-ID") String requestId,
        @RequestBody PaymentRequestDTO req) {
        
        return null;
        
    }





}
