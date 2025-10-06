package com.gharana.hotel_service.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gharana.hotel_service.dto.LocationDTO;
import com.gharana.hotel_service.entity.Hotel;
import com.gharana.hotel_service.service.HotelService;
import com.gharana.hotel_service.service.LocationService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/locations")
@AllArgsConstructor
public class MainController {

    private final LocationService locationService;
    private final HotelService hotelService;

    @GetMapping("")
    public ResponseEntity<List<LocationDTO>> getAllLocations() {
        return ResponseEntity.ok(locationService.getAllLocations());
    }

    @GetMapping("/{locationId}/hotels")
    public ResponseEntity<List<Hotel>> getHotelsbyLocationId(@PathVariable Long locationId) {
        return ResponseEntity.ok(hotelService.getHotelsByLocationId(locationId));
    }

}
