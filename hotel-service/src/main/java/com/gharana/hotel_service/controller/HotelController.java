package com.gharana.hotel_service.controller;

import com.gharana.hotel_service.model.Hotel;
import com.gharana.hotel_service.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/hotels")
public class HotelController {

    @Autowired
    private HotelService hotelService;

    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @GetMapping("")
    public List<Hotel> getHotelsByDestination(@RequestParam("destination") String destination) {
        // Validate input parameters
        return hotelService.getHotelsByDestination(destination);
    }
}
