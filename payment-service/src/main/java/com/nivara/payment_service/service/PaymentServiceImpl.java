package com.nivara.payment_service.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.nivara.payment_service.client.InventoryServiceClient;
import com.nivara.payment_service.model.dto.HoldDTO;
import com.nivara.payment_service.model.dto.PaymentRequestDTO;
import com.nivara.payment_service.model.dto.PaymentResponseDTO;
import com.nivara.payment_service.model.entity.Payment;
import com.nivara.payment_service.model.enums.PaymentStatus;
import com.nivara.payment_service.repository.PaymentRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final InventoryServiceClient inventoryServiceClient;
    private final RazorpayClient razorpayClient;

    @Override
    public PaymentResponseDTO createPayment(String requestId, PaymentRequestDTO requestBody) {
        
        // Step 1: Perform idempotency check to ensure if this request is already processed
        Optional<Payment> existing = paymentRepository.findByRequestId(requestId);
        if(existing.isPresent()) {
            Payment payment = existing.get();
            if("SUCCESS".equalsIgnoreCase(String.valueOf(payment.getStatus()))) {
                return PaymentResponseDTO.builder()
                    .providerOrderId(payment.getProviderOrderId())
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
        Payment payment = Payment.builder()
            .requestId(requestId)
            .customer(requestBody.getCustomer())
            .amount(requestBody.getAmount())
            .currency(requestBody.getCurrency())
            .status(PaymentStatus.PENDING)
            .build();
        payment = paymentRepository.save(payment);

        try {
            // Build Razorpay Order payload (amount MUST be in paise/smallest unit)
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", requestBody.getAmount() * 100);
            orderRequest.put("currency", requestBody.getCurrency() != null ? requestBody.getCurrency() : "INR");
            orderRequest.put("receipt", "hold_" + requestBody.getHoldId());
            orderRequest.put("payment_capture", 0);

            Order order = razorpayClient.orders.create(orderRequest);
            payment.setProviderOrderId(order.get("id"));
            paymentRepository.save(payment);

            return PaymentResponseDTO.builder()
                .providerOrderId(order.get("id"))
                .build();

        } catch (Exception e) {
            payment.setStatus(PaymentStatus.FAILED);
            paymentRepository.save(payment);

            return PaymentResponseDTO.builder()
                .status(PaymentStatus.FAILED)
                .message("gateway error: " + e.getMessage())
                .build();
        }
        
    }

}
