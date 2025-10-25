package com.gharana.inventory_service.model.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class HoldDTO {
    private final Long holdId;
    private boolean created; // true if a new hold was created, false if existing active hold was returned
    private LocalDateTime expiresAt;
    private String message;
}
