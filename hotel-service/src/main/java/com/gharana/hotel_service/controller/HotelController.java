package com.gharana.hotel_service.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gharana.hotel_service.dto.HotelDTO;
import com.gharana.hotel_service.service.HotelService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/hotels")
@AllArgsConstructor
public class HotelController {

    private final HotelService hotelService;

    @GetMapping("")
    public ResponseEntity<List<HotelDTO>> getHotelsbyLocation(@RequestParam Long locationId) {
        return ResponseEntity.ok(hotelService.getHotelsByLocation(locationId));
    }

    @GetMapping("/{hotelId}")
    public ResponseEntity<HotelDTO> getHotel(@PathVariable Long hotelId) {
        return null;
    }





}
