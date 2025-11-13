package com.nivara.payment_service.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nivara.payment_service.model.dto.CreatePaymentOrderRequestDTO;
import com.nivara.payment_service.model.dto.CreatePaymentOrderResponseDTO;
import com.nivara.payment_service.service.PaymentService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/payments")
@AllArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/create-order")
    public ResponseEntity<CreatePaymentOrderResponseDTO> createPaymentOrder(@RequestBody CreatePaymentOrderRequestDTO requestBody) {

        Payment response = paymentService.createPayment(
            requestBody.getReservationId(),
            requestBody.getAmount(),
            requestBody.getCurrency(),
            );
        
        // if(resp.isSuccess()) {
        //     return ResponseEntity.status(409).body(resp);
        // }

        // URI location = URI.create("/api/payments/" + resp.getPaymentId());
        // return ResponseEntity.created(location).body(resp);

        return null;
        
    }

    @PostMapping("/verify")
    public ResponseEntity<String> finalizePayment(@RequestHeader("X-Razorpay-Signature") String signatureHeader) {
        return null;
    }

}
