package com.nivara.reservation_service.model.dto;

import java.time.Instant;

public record CreateHoldResponse(
    Long holdId,
    Instant expiresAt,
    Long lockedAmount // to ensure the orchestrator knows the price that's locked
) {}


