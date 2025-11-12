package com.gharana.inventory_service.model.dto;

public record ErrorResponseDTO (
    String code,
    String message,
    Object details,
    String timestamp
) {}
