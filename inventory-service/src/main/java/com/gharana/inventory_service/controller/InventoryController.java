package com.gharana.inventory_service.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gharana.inventory_service.model.dto.AvailableRoomTypeDTO;
import com.gharana.inventory_service.model.dto.HoldDTO;
import com.gharana.inventory_service.model.dto.HoldInventoryRequestDTO;
import com.gharana.inventory_service.model.dto.RoomAvailabilityRequestDTO;
import com.gharana.inventory_service.service.InventoryService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("api/inventory")
@AllArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping("/get-availability")
    public ResponseEntity<List<AvailableRoomTypeDTO>> getRoomAvailability(@RequestBody RoomAvailabilityRequestDTO request) {
            return ResponseEntity.ok().body(inventoryService.getAvailableRoomTypes(request.getHotelIds(), request.getCheckInDate(), request.getCheckOutDate()));
    }

    @PostMapping("/hold")
    public ResponseEntity<HoldDTO> holdSelectedInventory(
        @RequestHeader(value = "X-Request-ID") String requestId,
        @RequestBody HoldInventoryRequestDTO req) {

        return null;
    
    }

}
