package com.gharana.search_service.dto;

import java.util.List;

import lombok.Data;

@Data
public class HotelDTO {
    private String id;
    private String name;
    private String locationId;
    private String address;
    private String description;
    private String thumbnailUrl;
    private String rating;
    private List<AmenityDTO> amenities;
}
