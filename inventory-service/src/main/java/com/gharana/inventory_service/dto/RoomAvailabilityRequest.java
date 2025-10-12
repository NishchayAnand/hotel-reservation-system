package com.gharana.inventory_service.dto;

import java.time.LocalDate;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class RoomAvailabilityRequest {
    private Set<Long> hotelIds;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
}
