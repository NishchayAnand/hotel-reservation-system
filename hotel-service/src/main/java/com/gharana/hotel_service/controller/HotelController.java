package com.gharana.hotel_service.controller;

import com.gharana.hotel_service.dto.HotelDTO;
import com.gharana.hotel_service.service.HotelService;

import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/hotels")
@AllArgsConstructor
public class HotelController {

    private final HotelService hotelService;

    @GetMapping("")
    public ResponseEntity<List<HotelDTO>> getHotelsByLocation(@RequestParam String city, 
                                            @RequestParam String state,
                                            @RequestParam String country) {
        // Validate input parameters
        List<HotelDTO> hotels = hotelService.getHotelsByLocation(city, state, country);
        return ResponseEntity.ok(hotels);
    }
}
