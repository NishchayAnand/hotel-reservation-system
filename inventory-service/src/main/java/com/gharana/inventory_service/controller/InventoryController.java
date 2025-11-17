package com.gharana.inventory_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gharana.inventory_service.model.dto.AvailableRoomTypeDTO;
import com.gharana.inventory_service.model.dto.CreateHoldRequestDTO;
import com.gharana.inventory_service.model.dto.CreateHoldResponseDTO;
import com.gharana.inventory_service.model.dto.HoldDTO;
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
        Hold hold = inventoryService.createHold(
            requestBody.reservationId(), 
            requestBody.hotelId(), 
            requestBody.checkInDate(), 
            requestBody.checkOutDate(),
            requestBody.reservationItems()
        );

        // map entity -> DTO using factory and return created
        CreateHoldResponseDTO responseBody = CreateHoldResponseDTO.from(hold);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
    }

    @GetMapping("/holds/{holdId}")
    public ResponseEntity<HoldDTO> getHold(@RequestParam Long holdId) {
        HoldDTO responseBody = inventoryService.getHold(holdId);
        return ResponseEntity.ok().body(responseBody);
    }

}
