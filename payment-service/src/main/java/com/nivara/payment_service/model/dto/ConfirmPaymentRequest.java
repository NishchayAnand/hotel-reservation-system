package com.nivara.payment_service.model.dto;

public record ConfirmPaymentRequest (
    Long reservationId,
    Long holdId,
    String razorpayPaymentId,
    String razorpayOrderId,
    String razorpaySignature
) {}
