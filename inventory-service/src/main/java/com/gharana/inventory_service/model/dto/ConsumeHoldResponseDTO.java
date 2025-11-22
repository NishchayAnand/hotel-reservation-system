package com.gharana.inventory_service.model.dto;

import com.gharana.inventory_service.model.enums.HoldStatus;

public record ConsumeHoldResponseDTO(
    Long holdId,
    HoldStatus status,
    Long reservationId,
    Long paymentId
) {}
