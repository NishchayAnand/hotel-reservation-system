package com.nivara.reservation_service.model.dto;

import java.time.Instant;

public record CreateHoldResponseDTO(Long holdId, String status, Instant expiresAt) {}


