package com.gharana.pricing_service.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface PricingService {

    public List<Double> getPrice(String hotelId, String roomTypeId, LocalDate checkInDate, LocalDate checkOutDate);
    
}
