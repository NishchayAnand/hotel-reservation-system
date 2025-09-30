package com.gharana.hotel_service.controller;

import com.gharana.hotel_service.model.Hotel;
import com.gharana.hotel_service.service.HotelService;

import lombok.AllArgsConstructor;

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
    public List<Hotel> getHotelsByDestination(@RequestParam("destination") String destination) {
        // Validate input parameters
        return hotelService.getHotelsByDestination(destination);
    }
}
