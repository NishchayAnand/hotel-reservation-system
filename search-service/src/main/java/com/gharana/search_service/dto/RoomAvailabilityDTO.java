package com.gharana.search_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RoomAvailabilityDTO {
    private Long hotelId;
    private Long roomTypeId;
    private int availableCount;
}
