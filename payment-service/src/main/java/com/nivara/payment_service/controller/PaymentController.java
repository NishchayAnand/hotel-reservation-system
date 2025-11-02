package com.nivara.payment_service.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nivara.payment_service.model.dto.PaymentRequestDTO;
import com.nivara.payment_service.model.dto.PaymentResponseDTO;
import com.nivara.payment_service.service.PaymentService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/payments")
@AllArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/create-order")
    public ResponseEntity<PaymentResponseDTO> createOrder(
        @RequestHeader(value = "X-Request-ID") String requestId,
        @RequestBody PaymentRequestDTO req) 
    {

        PaymentResponseDTO resp = paymentService.createPayment(requestId, req);
        
        if(resp.isSuccess()) {
            return ResponseEntity.status(409).body(resp);
        }

        URI location = URI.create("/api/payments/" + resp.getPaymentId());
        return ResponseEntity.created(location).body(resp);
        
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyPayment(
        @RequestHeader("X-Razorpay-Signature") String signatureHeader
    ) {
        return null;
    }







}
