package com.gharana.inventory_service.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.HttpHeaders;
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

    @PostMapping("/holds")
    public ResponseEntity<String> holdSelectedInventory(
        @RequestHeader(value = "X-Request-ID") String requestId,
        @RequestBody HoldInventoryRequestDTO req) {
        
        try {
            HoldDTO resp = inventoryService.createInventoryHold(requestId, 
                req.getHotelId(), 
                req.getCheckInDate(), 
                req.getCheckOutDate(), 
                req.getSelectedInventory());

            if(!resp.isSuccess()) {
                // business failure -> insufficient inventory -> 409 Conflict
                return ResponseEntity.status(409).body(resp.getMessage());
            }

            URI location = URI.create("api/inventory/holds/" + resp.getHoldId());
            if(resp.isCreated()) {
                return ResponseEntity.created(location).body(resp.getMessage());
            }

            return ResponseEntity.ok().header(HttpHeaders.LOCATION, location.toString()).body(resp.getMessage());   
        } catch (Exception ex) {
            return ResponseEntity.status(502).body("error occurred");
        }
    
    }

}
