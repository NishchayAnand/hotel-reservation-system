package com.nivara.payment_service.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.nivara.payment_service.client.InventoryServiceClient;
import com.nivara.payment_service.model.dto.HoldDTO;
import com.nivara.payment_service.model.dto.PaymentRequestDTO;
import com.nivara.payment_service.model.dto.PaymentResponseDTO;
import com.nivara.payment_service.model.entity.Payment;
import com.nivara.payment_service.model.enums.PaymentStatus;
import com.nivara.payment_service.repository.PaymentRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final InventoryServiceClient inventoryServiceClient;

    @Override
    public PaymentResponseDTO processPayment(String requestId, PaymentRequestDTO requestBody) {
        
        // Step 1: Perform idempotency check to ensure if this request is already processed
        Optional<Payment> existing = paymentRepository.findByRequestId(requestId);
        if(existing.isPresent()) {
            Payment payment = existing.get();
            if("SUCCESS".equalsIgnoreCase(String.valueOf(payment.getStatus()))) {
                return PaymentResponseDTO.builder()
                    .status(payment.getStatus())
                    .message("payment already processed")
                    .build();
            }
            // if not SUCCESS, we continue and handle as new attempt 
        }

        // Step 2: Validate hold with inventory service
        HoldDTO hold = inventoryServiceClient.getHold(requestBody.getHoldId());
        if(!"ACTIVE".equalsIgnoreCase(String.valueOf(hold.getStatus()))) {
            return PaymentResponseDTO.builder()
                .status(PaymentStatus.FAILED)
                .message("hold is not active")
                .build();
        }

        // safety margin: ensure hold has at least 2 minutes remaining (avoid redirect and immediate expiry)
        Instant expiresAt = hold.getExpiresAt();
        if(Instant.now().isAfter(expiresAt.minus(2, ChronoUnit.MINUTES))) {
            return PaymentResponseDTO.builder()
                .status(PaymentStatus.FAILED)
                .message("hold is expiring soon or already expired; refresh hold before paying again")
                .build();
        }

        // Step 3: Create Payment record (PENDING)
        // Q1. When should we use builder pattern
        // Q2. Is Payment table same as ledger??

        
        return null;

        
    }

}
