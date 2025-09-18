package com.gharana.pricing_service.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class PricingRecord {
    private String hotelId;
    private String roomTypeId;
    private LocalDate date;
    private double price;

    @Override
    public String toString() {
        return "\nPricingRecord: [hotelId=" + hotelId + ", roomTypeId=" + roomTypeId + ", date=" + date + ", price=" + price + "]";
    }
}
