package com.nivara.payment_service.model.dto;

public record ConfirmPaymentRequest (
    Long reservationId,
    Long holdId,
    String providerPaymentId,
    String providerOrderId,
    String providerSignature
) {}
