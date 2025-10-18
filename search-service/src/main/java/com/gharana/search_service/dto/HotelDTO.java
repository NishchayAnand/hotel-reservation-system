package com.gharana.search_service.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HotelDTO {
    private Long id;
    private String name;
    private String address;
    private String shortDescription;
    private String longDescription;
    private String thumbnailUrl;
    private Integer rating;
    private List<AmenityDTO> amenities;
}
