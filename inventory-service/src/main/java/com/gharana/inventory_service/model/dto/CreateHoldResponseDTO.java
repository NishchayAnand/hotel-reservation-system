package com.gharana.inventory_service.model.dto;

import java.time.Instant;

import com.gharana.inventory_service.model.entity.Hold;

import lombok.Data;

@Data
public class CreateHoldResponseDTO {
    private Long holdId;
    private String status;
    private Instant expiresAt;

    // factory to map Hold -> DTO
    public static CreateHoldResponseDTO from(Hold hold) {
        CreateHoldResponseDTO dto = new CreateHoldResponseDTO();
        dto.setHoldId(hold.getId());
        dto.setStatus(hold.getStatus().toString());
        dto.setExpiresAt(hold.getExpiresAt());
        return dto;     
    }
}
