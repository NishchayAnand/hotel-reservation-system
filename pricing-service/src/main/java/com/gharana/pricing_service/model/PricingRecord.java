package com.gharana.pricing_service.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
@Entity
@Table(name = "room_type_rate")
public class PricingRecord {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "hotel_id", nullable = false)
    private String hotelId;

    @Column(name = "room_type_id", nullable = false)
    private String roomTypeId;

    @Column(name = "reservation_date", nullable = false)
    private LocalDate reservationDate;

    
    private BigDecimal rate;
}
