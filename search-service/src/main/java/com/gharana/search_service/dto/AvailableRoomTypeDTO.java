package com.gharana.search_service.dto;

import java.math.BigDecimal;
import java.util.Set;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AvailableRoomTypeDTO {
    private Long id;
    private String name;
    private String description;
    private String bedType;
    private int bedCount;
    private String thumbnailUrl;
    private Integer availableRoomCount;
    private BigDecimal avgPricePerNight;
    private Set<AmenityDTO> amenities;
}
