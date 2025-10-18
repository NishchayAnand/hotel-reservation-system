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
    private String shortDescription;
    private String thumbnailUrl;
    private Integer rating;
    private BigDecimal minAvgRatePerNight; 
    private List<AmenityDTO> amenities;
}
