package com.nivara.reservation_service.model.dto;

import java.time.Instant;

public record CreateHoldResponse(
    String holdId,
    Instant expiresAt,
    Long amount // to ensure the orchestrator knows the price that's locked

) {}


