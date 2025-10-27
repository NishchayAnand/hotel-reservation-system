package com.nivara.payment_service.model.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class PaymentRequestDTO {
    private Long holdId;
    private BigDecimal amount;
    private String currency;
    private CustomerDTO customer;
}
