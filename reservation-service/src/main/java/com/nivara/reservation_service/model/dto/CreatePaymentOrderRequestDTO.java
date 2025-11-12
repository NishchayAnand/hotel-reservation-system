package com.nivara.reservation_service.model.dto;

public record CreatePaymentOrderRequestDTO(Long reservationId, Long amount, String currency) {}
