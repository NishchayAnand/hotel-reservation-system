package com.gharana.search_service.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RoomTypeAvailabilityDTO {
    private String hotelId;
    private String roomTypeId;
    private List<LocalDate> availableDates;
}
