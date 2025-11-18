package com.nivara.reservation_service.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.nivara.reservation_service.model.dto.ReservationDTO;
import com.nivara.reservation_service.model.dto.ReservationItemDTO;
import com.nivara.reservation_service.model.entity.Reservation;
import com.nivara.reservation_service.model.entity.ReservationItem;

// Immutable class
public final class ReservationMapper {

    private ReservationMapper() {}

    public static ReservationDTO toDto(Reservation reservation) {
        List<ReservationItemDTO> reservedItems = reservation.getReservationItems().stream()
            .map(ReservationMapper::itemToDto)
            .collect(Collectors.toList());

        return new ReservationDTO(
            reservation.getId(),
            reservation.getHotelId(),
            reservation.getCheckInDate(),
            reservation.getCheckOutDate(),
            reservedItems,
            reservation.getAmount(),
            reservation.getCurrency(),
            reservation.getHoldId(),
            reservation.getExpiresAt(),
            reservation.getStatus(),
            reservation.getCreatedAt(),
            reservation.getUpdatedAt()
        );     
    }

    private static ReservationItemDTO itemToDto(ReservationItem item) {
        return new ReservationItemDTO(
            item.getRoomTypeId(),
            item.getQuantity(),
            item.getRate()
        );

    }
}
