package com.gharana.hotel_service.model.dto;

import java.util.Set;

import com.gharana.hotel_service.model.entity.Amenity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HotelDTO {
    private Long id;
    private String name;
    private String address;
    private String shortDescription;
    private String longDescription;
    private String thumbnailUrl;
    private Integer rating;
    private Set<Amenity> amenities;
}
