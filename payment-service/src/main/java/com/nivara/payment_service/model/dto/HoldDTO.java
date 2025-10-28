package com.nivara.payment_service.model.dto;

import java.time.Instant;

import com.nivara.payment_service.model.enums.HoldStatus;

import lombok.Data;

@Data
public class HoldDTO {
    private Long id;
    private HoldStatus status;
    private Instant expiresAt;
}
