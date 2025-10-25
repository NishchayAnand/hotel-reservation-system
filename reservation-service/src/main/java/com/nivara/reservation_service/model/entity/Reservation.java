package com.nivara.reservation_service.model.entity;

import java.time.LocalDate;
import java.util.List;

import com.nivara.reservation_service.model.dto.ReservationItemDTO;
import com.nivara.reservation_service.model.enums.ReservationStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "reservations")
@Data
public class Reservation {
    private Long id;
    private String requestId;
    private Long hotelId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    List<ReservationItemDTO> reservationItems;
    private ReservationStatus status;
}
