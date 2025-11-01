package com.nivara.payment_service.model.dto;

import lombok.Data;

@Data
public class PaymentRequestDTO {
    private Long holdId;
    private Long amount;
    private String currency;
    private CustomerDTO customer;
}
