package com.nivara.payment_service.model.dto;

import com.nivara.payment_service.model.enums.PaymentStatus;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentResponseDTO {
    private PaymentStatus status;
}
