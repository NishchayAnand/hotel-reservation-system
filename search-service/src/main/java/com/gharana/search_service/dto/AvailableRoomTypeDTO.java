package com.gharana.search_service.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AvailableRoomTypeDTO {
    private Long id;
    private String name;
    private int capacity;
    private int rating;
    private String thumbnailUrl;
    private int availableCount;
    private BigDecimal avgRatePerNight;
    private List<AmenityDTO> amenities;
}
