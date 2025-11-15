package com.nivara.payment_service.model.entity;

import com.nivara.payment_service.model.dto.CustomerDTO;
import com.nivara.payment_service.model.enums.PaymentStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reservation_id")
    private Long reservationId; 

    @Column(name = "total")
    private Long total;

    private String currency;

    private PaymentStatus status;

    @Column(name = "provider_order_id")
    private String providerOrderId;

    // do we need a customer table / entity for customer details as of now
    private CustomerDTO customer;

}
