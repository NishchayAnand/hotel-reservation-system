package com.gharana.pricing_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gharana.pricing_service.dto.PricingQueryRequest;
import com.gharana.pricing_service.service.PricingService;

@RestController
@RequestMapping("/pricing")
public class PricingController {

    @Autowired
    private PricingService pricingService;

    public PricingController(PricingService pricingService) {
        this.pricingService = pricingService;
    }

    @PostMapping("/")
    public List<Double> getPrice(@RequestBody PricingQueryRequest req) {
        return pricingService.getPrice(req.getHotelId(), req.getRoomTypeId(), req.getCheckInDate(), req.getCheckOutDate());
    }

}
