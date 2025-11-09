package com.gharana.inventory_service.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import com.gharana.inventory_service.model.dto.AvailableRoomTypeDTO;
import com.gharana.inventory_service.model.dto.ReservationItemDTO;
import com.gharana.inventory_service.model.entity.Hold;

public interface InventoryService {
    
    List<AvailableRoomTypeDTO> getAvailableRoomTypes(
        Set<Long> hotelIds, 
        LocalDate checkInDate, 
        LocalDate checkOutDate
    );
    
    Hold createHold(
        Long reservationId, 
        Long hotelId, 
        LocalDate checkInDate, 
        LocalDate checkOutDate, 
        List<ReservationItemDTO> selectedRooms
    );

}
