package com.gharana.hotel_service.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gharana.hotel_service.model.dto.LocationDTO;
import com.gharana.hotel_service.service.LocationService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/locations")
@CrossOrigin(origins = {"http://localhost:3000", "https://main.d3h5opq9bz2w4u.amplifyapp.com"})
@AllArgsConstructor
public class LocationController {

    private final LocationService locationService;
    
    @GetMapping("")
    public ResponseEntity<List<LocationDTO>> getAllLocations() {
        return ResponseEntity.ok(locationService.getAllLocations());
    }

    

}
