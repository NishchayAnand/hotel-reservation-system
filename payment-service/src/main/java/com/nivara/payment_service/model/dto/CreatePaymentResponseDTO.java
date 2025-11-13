package com.nivara.payment_service.model.dto;

import com.nivara.payment_service.model.entity.Payment;
import lombok.Data;

@Data
public class CreatePaymentResponseDTO {
    private final Long paymentId;
    private final String providerOrderId;   // Razorpay order id
    private final String paymentStatus;      // e.g. PENDING, COMPLETED, FAILED
    private String message;

    public static CreatePaymentResponseDTO from(Payment payment) {
        return new CreatePaymentResponseDTO(
            payment.getId(),
            payment.getProviderOrderId(), 
            payment.getStatus().toString()
        );
    }
}
