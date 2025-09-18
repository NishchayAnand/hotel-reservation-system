package com.gharana.pricing_service.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

@Service
public interface PricingService {
    public double getAvgPricePerNight(String hotelId, String roomTypeId, LocalDate checkInDate, LocalDate checkOutDate);
}
