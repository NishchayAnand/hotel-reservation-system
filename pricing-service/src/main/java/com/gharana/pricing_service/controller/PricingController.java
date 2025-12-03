package com.gharana.pricing_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gharana.pricing_service.dto.PricingQueryRequestDTO;
import com.gharana.pricing_service.dto.AvgRoomTypePriceQuoteDTO;
import com.gharana.pricing_service.dto.MinHotelPriceQuoteDTO;
import com.gharana.pricing_service.service.PricingService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("api/pricing")
@AllArgsConstructor
public class PricingController {

    private final PricingService pricingService;

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("OK");
    }

    @PostMapping("/hotels")
    public ResponseEntity<List<MinHotelPriceQuoteDTO>> getMinHotelPricePerNight(@RequestBody PricingQueryRequestDTO req) {
        // return average price per night for each available room type for the selected [checkInDate, checkOutDate) daterange.
        return ResponseEntity.ok().body(pricingService.getMinHotelRatePerNight(req.getAvailableRoomTypes(), req.getCheckInDate(), req.getCheckOutDate()));
    }

    @PostMapping("/room-types")
    public ResponseEntity<List<AvgRoomTypePriceQuoteDTO>> getAvgRoomTypePricePerNight(@RequestBody PricingQueryRequestDTO req) {
        List<AvgRoomTypePriceQuoteDTO> avgPriceQuotes = pricingService.getAvgRoomTypePricePerNight(req.getAvailableRoomTypes(), req.getCheckInDate(), req.getCheckOutDate());
        return ResponseEntity.ok().body(avgPriceQuotes);
    }


}
