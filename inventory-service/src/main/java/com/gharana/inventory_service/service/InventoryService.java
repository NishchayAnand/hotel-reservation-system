package com.gharana.inventory_service.service;

import java.time.LocalDate;
import java.util.List;

import com.gharana.inventory_service.dto.AvailableRoomTypeDTO;

public interface InventoryService {
    List<AvailableRoomTypeDTO> queryRoomAvailability(List<Long> hotelIds, LocalDate checkInDate, LocalDate checkOutDate);
}
