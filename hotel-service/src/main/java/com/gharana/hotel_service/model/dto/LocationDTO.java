package com.gharana.hotel_service.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LocationDTO {
    private Long id;
    private String city;
}
