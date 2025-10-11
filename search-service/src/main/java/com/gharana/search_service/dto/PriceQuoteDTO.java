package com.gharana.search_service.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PriceQuoteDTO {
    private Long hotelId;
    private Long roomTypeId;
    private BigDecimal avgRatePerNight;

}
