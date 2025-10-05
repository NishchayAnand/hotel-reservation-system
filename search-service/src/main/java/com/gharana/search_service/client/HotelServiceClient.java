package com.gharana.search_service.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gharana.search_service.dto.Hotel;

@FeignClient(name = "hotel-service", url = "http://localhost:8081")
public interface HotelServiceClient {

    @GetMapping("/hotels")
    List<Hotel> getHotelsByDestination(@RequestParam String city);
}
