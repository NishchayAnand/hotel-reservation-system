package com.gharana.inventory_service.model.dto;

import java.time.Instant;

import com.gharana.inventory_service.model.enums.HoldStatus;

import lombok.Data;

@Data
public class HoldDTO {
    private final Long id;
    private final HoldStatus status;
    private final Instant expiresAt;
}
