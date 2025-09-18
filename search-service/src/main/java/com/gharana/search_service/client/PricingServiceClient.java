package com.gharana.search_service.client;

import java.time.LocalDate;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="pricing-service", url="http://localhost:8083/pricing")
public interface PricingServiceClient {

    @GetMapping("/query")
    double getAvgPricePerNight(
        @RequestParam("hotelId") String hotelId,
        @RequestParam("roomTypeId") String roomTypeId,
        @RequestParam("checkInDate") LocalDate checkInDate,
        @RequestParam("checkOutDate") LocalDate checkOutDate);

}
