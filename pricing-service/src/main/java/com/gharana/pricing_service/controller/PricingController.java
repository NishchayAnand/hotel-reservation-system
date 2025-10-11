package com.gharana.pricing_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gharana.pricing_service.dto.PricingQueryRequestDTO;
import com.gharana.pricing_service.dto.PriceQuoteDTO;
import com.gharana.pricing_service.service.PricingService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/pricing")
@AllArgsConstructor
public class PricingController {

    private final PricingService pricingService;

    @PostMapping("/query")
    public ResponseEntity<List<PriceQuoteDTO>> getAvgPricePerNight(@RequestBody PricingQueryRequestDTO req) {
        // return average price per night for each available room type for the selected [checkInDate, checkOutDate) daterange.
        return ResponseEntity.ok().body(pricingService.getAvgRatePerNight(req.getAvailableRoomTypes(), req.getCheckInDate(), req.getCheckOutDate()));
    }

}
