package com.gharana.pricing_service.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AvgRoomTypePriceQuoteDTO {
    private Long hotelId;
    private Long roomTypeId;
    private BigDecimal avgPricePerNight;
}
