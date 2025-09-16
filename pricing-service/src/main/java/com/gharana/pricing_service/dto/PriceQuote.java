package com.gharana.pricing_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class PriceQuote {
    private String hotelId;
    private String roomTypeId;
    private double avgPricePerNight; // change this to BigDecimal for precision
}
