package com.nivara.payment_service.model.dto;

public record CreatePaymentRequestDTO (
    Long reservationId,
    Long holdId,
    long total,
    String currency,
    CustomerDTO customer
) {}
