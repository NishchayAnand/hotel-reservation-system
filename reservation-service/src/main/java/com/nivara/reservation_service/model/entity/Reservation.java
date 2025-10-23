package com.nivara.reservation_service.model.entity;

import com.nivara.reservation_service.model.enums.ReservationStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "reservations")
@Data
public class Reservation {
    private Long id;
    private ReservationStatus status;
}
