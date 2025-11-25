package com.nivara.payment_service.model.dto;

import com.nivara.payment_service.model.enums.HoldStatus;

public record ConsumeHoldResponseDTO(
    Long holdId,
    HoldStatus status,
    Long reservationId,
    Long paymentId
) {

    public boolean isSuccess() {
        return status != null && status == HoldStatus.CONSUMED;
    }
 
}
