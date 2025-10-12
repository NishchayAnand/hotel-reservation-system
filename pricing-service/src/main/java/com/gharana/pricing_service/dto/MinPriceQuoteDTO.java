package com.gharana.pricing_service.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MinPriceQuoteDTO {
    private Long hotelId;
    private BigDecimal minRatePerNight;
}
