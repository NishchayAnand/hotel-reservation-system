package com.gharana.search_service.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class AvgRoomTypePriceQuoteDTO {
    private Long hotelId;
    private Long roomTypeId;
    private BigDecimal avgPricePerNight;
}
