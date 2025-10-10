package com.gharana.search_service.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RoomAvailabilityRequest {
    private List<String> hotelIds;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
}
