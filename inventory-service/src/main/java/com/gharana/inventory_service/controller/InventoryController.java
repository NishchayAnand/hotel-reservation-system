package com.gharana.inventory_service.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gharana.inventory_service.model.dto.AvailableRoomTypeDTO;
import com.gharana.inventory_service.model.dto.CreateHoldRequestDTO;
import com.gharana.inventory_service.model.dto.CreateHoldResponseDTO;
import com.gharana.inventory_service.model.dto.RoomAvailabilityRequestDTO;
import com.gharana.inventory_service.model.entity.Hold;
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

    @PostMapping("/create-hold")
    public ResponseEntity<CreateHoldResponseDTO> createHold(@RequestBody CreateHoldRequestDTO requestBody) {
        
        try {

            Hold hold = inventoryService.createHold(
                requestBody.reservationId(), 
                requestBody.hotelId(), 
                requestBody.checkInDate(), 
                requestBody.checkOutDate(),
                requestBody.reservationItems()
            );

            return null;
            /* 
            if(!resp.isSuccess()) {
                // business failure -> insufficient inventory -> 409 Conflict
                return ResponseEntity.status(HttpStatus.CONFLICT).body(resp);
            }

            if(resp.isCreated()) {
                return ResponseEntity.status(HttpStatus.CREATED).body(resp);
            }

            return ResponseEntity.ok().body(resp);   
            */

        } catch (Exception ex) {
            return ResponseEntity.status(502).body(null);
        }
    
    }

}
