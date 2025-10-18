package com.gharana.search_service.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gharana.search_service.dto.AvailableHotelDTO;
import com.gharana.search_service.dto.AvailableRoomTypeDTO;
import com.gharana.search_service.service.SearchService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/hotels")
@CrossOrigin(origins = "http://localhost:3000")
@AllArgsConstructor
public class HotelController {

    private final SearchService searchService;

    @GetMapping("/hotel-listing")
    public ResponseEntity<List<AvailableHotelDTO>> getAvailableHotelsByLocationId(@RequestParam Long locationId, 
                                @RequestParam LocalDate checkInDate, 
                                @RequestParam LocalDate checkOutDate) {
        return ResponseEntity.ok().body(searchService.getAvailableHotelsByLocationId(locationId, checkInDate, checkOutDate));   
    }

    @GetMapping("/hotel-details/{hotelId}/room-types")
    public ResponseEntity<List<AvailableRoomTypeDTO>> getAvailableRoomTypesByHotelId(@PathVariable Long hotelId,
        @RequestParam LocalDate checkInDate,
        @RequestParam LocalDate checkOutDate) {
            return ResponseEntity.ok().body(searchService.getAvailableRoomTypesByHotelId(hotelId, checkInDate, checkOutDate));
        }


}
