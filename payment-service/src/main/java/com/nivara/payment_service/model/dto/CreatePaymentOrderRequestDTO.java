package com.nivara.payment_service.model.dto;

import lombok.Data;

@Data
public class CreatePaymentOrderRequestDTO {
    private Long reservationId;
    private Long amount;
    private String currency;
    private CustomerDTO customer;
}
