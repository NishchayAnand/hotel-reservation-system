package com.gharana.inventory_service.model.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HoldDTO {
    private Long holdId;
    private boolean success;
    private boolean created; // true if a new hold was created, false if existing active hold was returned
    private LocalDateTime expiresAt;
    private String message;
}
