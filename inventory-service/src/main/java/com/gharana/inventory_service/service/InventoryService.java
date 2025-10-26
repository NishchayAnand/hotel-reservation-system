package com.gharana.inventory_service.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import com.gharana.inventory_service.model.dto.AvailableRoomTypeDTO;
import com.gharana.inventory_service.model.dto.HoldDTO;
import com.gharana.inventory_service.model.dto.SelectedRoomTypeInventoryDTO;

public interface InventoryService {
    
    List<AvailableRoomTypeDTO> getAvailableRoomTypes(Set<Long> hotelIds, LocalDate checkInDate, LocalDate checkOutDate);
    
    HoldDTO createInventoryHold(String requestId, 
        Long hotelId, 
        LocalDate checkInDate, 
        LocalDate checkOutDate, 
        List<SelectedRoomTypeInventoryDTO> selectedRooms);
        
}
