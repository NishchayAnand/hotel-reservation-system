package com.gharana.inventory_service.service;

import java.time.LocalDate;
import java.util.List;

import com.gharana.inventory_service.dto.RoomTypeDTO;

public interface InventoryService {
    List<RoomTypeDTO> queryRoomAvailability(List<String> hotelIds, LocalDate checkInDate, LocalDate checkOutDate);
}
