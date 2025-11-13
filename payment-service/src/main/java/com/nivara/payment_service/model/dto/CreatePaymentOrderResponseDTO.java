package com.nivara.payment_service.model.dto;

import com.nivara.payment_service.model.enums.PaymentStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreatePaymentOrderResponseDTO {
    private Long paymentId;
    private boolean success;
    private String providerOrderId;
    private PaymentStatus status;
    private String message;
}
