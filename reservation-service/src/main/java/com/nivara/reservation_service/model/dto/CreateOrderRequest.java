package com.nivara.reservation_service.model.dto;

public record CreateOrderRequest(
    Long holdId,
    Long amount,
    String currency
) {}
