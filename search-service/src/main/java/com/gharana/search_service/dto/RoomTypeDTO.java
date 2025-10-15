package com.gharana.search_service.dto;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RoomTypeDTO {
    private Long id;
    private String name;
    private String description;
    private String bedType;
    private Integer bedCount;
    private Set<AmenityDTO> amenities;
}
