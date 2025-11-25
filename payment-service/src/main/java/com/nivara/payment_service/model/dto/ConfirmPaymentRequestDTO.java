package com.nivara.payment_service.model.dto;

public record ConfirmPaymentRequestDTO (
    Long reservationId,
    Long holdId,
    String providerPaymentId,
    String providerOrderId,
    String providerSignature
) {}
