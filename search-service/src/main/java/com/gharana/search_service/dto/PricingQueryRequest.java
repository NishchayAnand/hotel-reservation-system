package com.gharana.search_service.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class PricingQueryRequest {
    private String hotelId;
    private String roomTypeId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
}
