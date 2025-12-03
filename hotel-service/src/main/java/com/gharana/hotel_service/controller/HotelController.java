package com.gharana.hotel_service.controller;

import java.util.List;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gharana.hotel_service.model.dto.HotelDTO;
import com.gharana.hotel_service.model.dto.RoomTypeDTO;
import com.gharana.hotel_service.service.HotelService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/hotels")
@CrossOrigin(origins = "http://localhost:3000")
@AllArgsConstructor
public class HotelController {

    private final HotelService hotelService;

    @GetMapping("")
    public ResponseEntity<List<HotelDTO>> getHotelsbyLocationId(@RequestParam Long locationId) {
        return ResponseEntity.ok(hotelService.getHotelsByLocationId(locationId));
    }

    @GetMapping("/{hotelId}")
    public ResponseEntity<HotelDTO> getHotelById(@PathVariable Long hotelId) {
        return ResponseEntity.ok().body(hotelService.getHotelById(hotelId));
    }

    @PostMapping("/{hotelId}/room-types")
    public ResponseEntity<List<RoomTypeDTO>> getRoomTypesByIds(@PathVariable Long hotelId, @RequestBody Set<Long> roomTypeIds) {
        return ResponseEntity.ok().body(hotelService.getRoomTypesByIds(hotelId, roomTypeIds));
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("OK");
    }

}
