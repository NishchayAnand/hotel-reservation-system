package com.gharana.hotel_service.dto;

import java.util.Set;

import com.gharana.hotel_service.entity.Amenity;
import com.gharana.hotel_service.entity.BedType;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoomTypeDTO {
    private Long id;
    private String name;
    private String description;
    private BedType bedType;
    private Integer bedCount;
    private Set<Amenity> amenities;
}
