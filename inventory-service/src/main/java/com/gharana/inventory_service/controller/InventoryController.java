package com.gharana.inventory_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
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
            return ResponseEntity.ok().body(inventoryService.getRoomAvailability(request.getHotelIds(), request.getCheckInDate(), request.getCheckOutDate()));
    }

    @PostMapping("/hold")
    public ResponseEntity<HoldDTO> holdInventory(
        @RequestHeader(value = "X-Request-ID") String requestId,
        @RequestBody HoldInventoryRequestDTO req) {

        HoldDTO hold = inventoryService.holdInventory(requestId, 
            req.getHotel_id(), 
            req.getCheckInDate(), 
            req.getCheckOutDate(),
            req.getSelectedRooms());
        return hold.isCreated() ? ResponseEntity.status(HttpStatus.CREATED).body(hold) : ResponseEntity.ok().body(hold);
    
    }

}
