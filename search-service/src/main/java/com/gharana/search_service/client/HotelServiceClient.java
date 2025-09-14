package com.gharana.search_service.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.gharana.search_service.model.Hotel;

@FeignClient(name = "hotel-service", url = "http://localhost:8081/hotels")
public interface HotelServiceClient {

    @GetMapping("/")
    List<Hotel> getHotelsByDestination(String destination);
}
