package com.nivara.payment_service.model.entity;

import com.nivara.payment_service.model.enums.PaymentStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "payments")
public class Payment {

    private Long id;

    private String requestId;

    private PaymentStatus status;


}
