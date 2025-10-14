package com.gharana.inventory_service.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gharana.inventory_service.dto.AvailableRoomTypeDTO;
import com.gharana.inventory_service.dto.RoomAvailabilityRequest;
import com.gharana.inventory_service.service.InventoryService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("api/inventory")
@AllArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping("/get-availability")
    public ResponseEntity<List<AvailableRoomTypeDTO>> getRoomAvailability(@RequestBody RoomAvailabilityRequest request) {
            return ResponseEntity.ok().body(inventoryService.getRoomAvailability(request.getHotelIds(), request.getCheckInDate(), request.getCheckOutDate()));
    }

}
