package com.gharana.search_service.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gharana.search_service.dto.Hotel;

@FeignClient(name = "hotel-service", url = "http://localhost:8081/hotels")
public interface HotelServiceClient {

    @GetMapping("")
    List<Hotel> getHotelsByDestination(@RequestParam String city, @RequestParam String state, @RequestParam String country);
}
