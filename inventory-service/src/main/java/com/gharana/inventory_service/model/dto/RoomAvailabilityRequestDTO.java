package com.gharana.inventory_service.model.dto;

import java.time.LocalDate;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class RoomAvailabilityRequestDTO {
    private Set<Long> hotelIds;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
}
