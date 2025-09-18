package com.gharana.pricing_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.time.LocalDate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gharana.pricing_service.service.PricingService;

@RestController
@RequestMapping("/pricing")
public class PricingController {

    @Autowired
    private PricingService pricingService;

    public PricingController(PricingService pricingService) {
        this.pricingService = pricingService;
    }

    @GetMapping("/query")
    public double getAvgPricePerNight(
        @RequestParam("hotelId") String hotelId,
        @RequestParam("roomTypeId") String roomTypeId,
        @RequestParam("checkInDate") LocalDate checkInDate,
        @RequestParam("checkOutDate") LocalDate checkOutDate) {
        return pricingService.getAvgPricePerNight(hotelId, roomTypeId, checkInDate, checkOutDate);
    }

}
