package com.gharana.search_service.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.gharana.search_service.dto.HotelDTO;

@FeignClient(name = "hotel-service", url = "http://localhost:8081/api")
public interface HotelServiceClient {

    @GetMapping("/locations/{locationId}/hotels")
    List<HotelDTO> getHotelsByLocationId(@PathVariable Long locationId);
}
