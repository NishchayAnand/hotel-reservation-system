package com.nivara.payment_service.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.nivara.payment_service.model.dto.PaymentRequestDTO;
import com.nivara.payment_service.model.dto.PaymentResponseDTO;
import com.nivara.payment_service.model.entity.Payment;
import com.nivara.payment_service.repository.PaymentRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    @Override
    public PaymentResponseDTO processPayment(String requestId, PaymentRequestDTO requestBody) {
        
        // Step 1: Do idempotency check to ensure if this request is already processed
        Optional<Payment> existing = paymentRepository.findByRequestId(requestId);
        if(existing.isPresent()) {
            Payment payment = existing.get();
            if("SUCCESS".equalsIgnoreCase(payment.getStatus().toString())) {
                return new PaymentResponseDTO(payment.getStatus());
            }
        }

        // Step 2: Call Payment Gateway

        return null;
        
    }

}
