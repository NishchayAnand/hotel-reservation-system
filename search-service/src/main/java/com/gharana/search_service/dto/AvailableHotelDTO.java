package com.gharana.search_service.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class AvailableHotelDTO {
    private Long id;
    private String name;
    private String address;
    private String description;
    private String thumbnailUrl;
    private String rating;
    private List<AmenityDTO> amenities;
    private long nights;
    private BigDecimal avgRatePerNight; 
}
