package com.nivara.payment_service.model.entity;

import com.nivara.payment_service.model.dto.CustomerDTO;
import com.nivara.payment_service.model.enums.PaymentStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Entity
@Table(name = "payments")
public class Payment {

    private Long id;

    private String requestId; // should use holdId instead -- figure out why?

    private CustomerDTO customer;

    private String providerOrderId;

    private Long amount;

    private String currency;

    private PaymentStatus status;

}
