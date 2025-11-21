package com.nivara.payment_service.model.dto;

public record CreatePaymentRequestDTO (
    Long reservationId,
    Long holdId,
    long amount,
    String currency,
    String guestName,
    String guestEmail,
    String guestPhone
) {}
