package com.gharana.hotel_service.model.dto;

import java.util.Set;

import com.gharana.hotel_service.model.entity.Amenity;
import com.gharana.hotel_service.model.enums.BedType;

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
