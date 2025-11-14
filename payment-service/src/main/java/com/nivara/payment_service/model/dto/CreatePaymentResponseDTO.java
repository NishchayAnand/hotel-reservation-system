package com.nivara.payment_service.model.dto;

import com.nivara.payment_service.model.enums.PaymentStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreatePaymentResponseDTO {
    private final String providerOrderId;   // Razorpay order id
    private final PaymentStatus status;      // e.g. PENDING, COMPLETED, FAILED
    private String message;
}
