package com.gharana.inventory_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gharana.inventory_service.dto.AvailableRoomType;
import com.gharana.inventory_service.dto.InventoryQueryRequest;
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
    public List<AvailableRoomType> getAvailableRoomTypes(@RequestBody InventoryQueryRequest req) {
        return inventoryService.getAvailableRoomTypes(req.getHotelIds(), req.getCheckInDate(), req.getCheckOutDate());
    }

}
