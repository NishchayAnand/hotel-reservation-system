package com.gharana.pricing_service.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gharana.pricing_service.dto.RoomTypeRate;

@RestController
@RequestMapping("/pricing")
public class PricingController {

    @PostMapping("/")
    public Map<String, List<RoomTypeRate>> getRoomRates(List<String> hotelIds, LocalDate checkInDate, LocalDate checkOutDate) {
        // Implementation to fetch room rates for the given hotel IDs and date range
        return 
    }

}
