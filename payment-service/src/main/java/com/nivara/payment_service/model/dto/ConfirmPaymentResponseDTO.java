package com.nivara.payment_service.model.dto;

import com.nivara.payment_service.model.enums.PaymentStatus;

public record ConfirmPaymentResponseDTO(
    Long reservationId,
    Long paymentId,
    PaymentStatus status,
    String message
) {}
