package com.gharana.inventory_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gharana.inventory_service.dto.RoomTypeDTO;
import com.gharana.inventory_service.dto.RoomAvailabilityRequest;
import com.gharana.inventory_service.service.InventoryService;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping("/query")
    public List<RoomTypeDTO> queryRoomAvailability(@RequestBody RoomAvailabilityRequest request) {
            return inventoryService.queryRoomAvailability(request.getHotelIds(), request.getCheckInDate(), request.getCheckOutDate());
    }

}
