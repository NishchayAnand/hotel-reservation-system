package com.nivara.payment_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nivara.payment_service.model.dto.CreatePaymentRequestDTO;
import com.nivara.payment_service.model.dto.CreatePaymentResponseDTO;
import com.nivara.payment_service.service.PaymentService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/payments")
@AllArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/create-payment")
    public ResponseEntity<CreatePaymentResponseDTO> createPayment(@RequestBody CreatePaymentRequestDTO requestBody) {
        CreatePaymentResponseDTO response = paymentService.createPayment(requestBody);
        return ResponseEntity.status(200).body(response); 
    }

    @PostMapping("/confirm-payment")
    public ResponseEntity<String> confirmPayment(@RequestHeader("X-Razorpay-Signature") String signatureHeader) {
        return null;
    }

}
