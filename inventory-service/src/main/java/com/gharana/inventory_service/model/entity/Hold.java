package com.gharana.inventory_service.model.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Table(name = "holds")
@Data
@AllArgsConstructor
public class Hold {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "hold_id", nullable = false, unique = true)
    private String requestId;

    @Column(name = "hotel_id", nullable = false)
    private Long hotelId;

    @Column(name = "check_in_date", nullable = false)
    private LocalDate checkInDate;

    @Column(name = " check_out_date", nullable = false)
    private LocalDate checkOutDate;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    
}
