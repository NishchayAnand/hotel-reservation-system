package com.nivara.payment_service.model.dto;

import com.nivara.payment_service.model.enums.PaymentStatus;

public record ConfirmPaymentResponseDTO(
    Long paymentId,
    PaymentStatus status,
    String message
) {}
