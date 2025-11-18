package com.nivara.reservation_service.mapper;

import java.util.List;

import com.nivara.reservation_service.model.dto.ReservationDTO;
import com.nivara.reservation_service.model.dto.ReservationItemDTO;
import com.nivara.reservation_service.model.entity.Reservation;

// Immutable class
public final class ReservationMapper {

    private ReservationMapper() {}

    public static ReservationDTO toDto(Reservation reservation) {
        List<ReservationItemDTO> reservedItems = reservation.getReservationItems()
            .stream()
            .map(item -> new ReservationItemDTO(item.getRoomTypeId(), item.getQuantity(), item.getRate()))
            .toList();

        return ReservationDTO.builder()
            .id(reservation.getId())
            .hotelId(reservation.getHotelId())
            .checkInDate(reservation.getCheckInDate())
            .checkOutDate(reservation.getCheckOutDate())
            .reservedItems(reservedItems)
            .amount(reservation.getAmount())
            .currency(reservation.getCurrency())
            .holdId(reservation.getHoldId())
            .expiresAt(reservation.getExpiresAt())
            .status(reservation.getStatus())
            .createdAt(reservation.getCreatedAt())
            .updatedAt(reservation.getUpdatedAt())
            .build();    
    }
}
