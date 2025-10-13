package com.gharana.search_service.dto;

import java.util.List;

import lombok.Data;

@Data
public class RoomTypeDTO {
    private final Long id;
    private final Long hotelId;
    private final String name;
    private final int capacity;
    private final int rating;
    private final List<AmenityDTO> amenities;
}
