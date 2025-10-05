package com.gharana.hotel_service.dto;

import java.math.BigDecimal;

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
}
