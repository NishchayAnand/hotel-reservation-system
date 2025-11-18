package com.nivara.reservation_service.model.entity;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import com.nivara.reservation_service.model.enums.ReservationStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    @OneToMany(
        mappedBy = "reservation", 
        cascade = CascadeType.ALL, 
        orphanRemoval = true,   // remove DB rows when removed from list
        fetch = FetchType.LAZY
    )
    private List<ReservationItem> reservationItems;

    private Long amount;

    private String currency;

    @Column(name = "hold_id")
    private Long holdId;

    @Column(name = "expires_at")
    private Instant expiresAt; // tells your system: If payment hasn’t been completed by expiresAt, this reservation can no longer be confirmed because the inventory lock has expired.

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "reservation_status")
    private ReservationStatus status;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

}
