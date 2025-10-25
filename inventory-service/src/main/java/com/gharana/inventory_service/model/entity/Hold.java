package com.gharana.inventory_service.model.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Data
@AllArgsConstructor
public class Hold {
    private Long holdId;
    private String requestId;
    private Long hotelId;
    private Long roomTypeId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Integer qty;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private String status;
}
