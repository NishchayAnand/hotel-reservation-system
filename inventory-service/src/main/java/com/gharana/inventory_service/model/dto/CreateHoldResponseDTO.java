package com.gharana.inventory_service.model.dto;

import java.time.Instant;

public record CreateHoldResponseDTO(
    Long holdId,
    Instant expiresAt
) {}
