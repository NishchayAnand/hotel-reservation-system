package com.gharana.hotel_service.dto;

import java.math.BigDecimal;
import java.util.Set;

import com.gharana.hotel_service.entity.Amenity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HotelDTO {
    private Long id;
    private String name;
    private String address;
    private String description;
    private String thumbnailUrl;
    private BigDecimal rating;
    private Set<Amenity> amenities;
}
