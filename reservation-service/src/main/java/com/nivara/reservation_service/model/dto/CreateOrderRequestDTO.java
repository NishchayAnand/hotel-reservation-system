package com.nivara.reservation_service.model.dto;

public record CreateOrderRequestDTO(
    Long holdId,
    Long amount,
    String currency
) {}
