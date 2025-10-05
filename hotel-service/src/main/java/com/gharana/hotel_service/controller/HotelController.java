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
@RequestMapping("/api")
@AllArgsConstructor
public class HotelController {

    private final HotelService hotelService;

    @GetMapping("/hotels")
    public ResponseEntity<List<HotelDTO>> getHotelsByDestination(@RequestParam String city) {
        // Validate input parameters
        List<HotelDTO> hotels = hotelService.getHotelsByCity(city);
        return ResponseEntity.ok(hotels);
    }
}
