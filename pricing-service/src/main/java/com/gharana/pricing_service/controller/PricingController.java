package com.gharana.pricing_service.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.time.LocalDate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gharana.pricing_service.service.PricingService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/pricing")
@AllArgsConstructor
public class PricingController {

    private PricingService pricingService;

    @PostMapping("/query")
    public double getAvgPricePerNight(
        @RequestParam("hotelId") String hotelId,
        @RequestParam("roomTypeId") String roomTypeId,
        @RequestParam("checkInDate") LocalDate checkInDate,
        @RequestParam("checkOutDate") LocalDate checkOutDate) {
        return pricingService.getAvgPricePerNight(hotelId, roomTypeId, checkInDate, checkOutDate);
    }

}
