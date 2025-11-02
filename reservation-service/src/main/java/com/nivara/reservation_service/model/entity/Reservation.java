package com.nivara.reservation_service.model.entity;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import com.nivara.reservation_service.model.enums.ReservationStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Entity
@Table(name = "reservations")
public class Reservation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "request_id")
    private String requestId;

    private Long hotelId;

    private LocalDate checkInDate;

    private LocalDate checkOutDate;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL)
    private List<ReservationItem> reservationItems;

    private long amount;

    private String currency;

    @Column(name = "hold_id")
    private Long holdId;

    @Column(name = "order_id")
    private String orderId;

    @Column(name = "expires_at")
    private Instant expiresAt; // tells your system: If payment hasn’t been completed by expiresAt, this reservation can no longer be confirmed because the inventory lock has expired.

    private ReservationStatus status;

    @Column(name = "created_at")
    private String createdAt;
}
