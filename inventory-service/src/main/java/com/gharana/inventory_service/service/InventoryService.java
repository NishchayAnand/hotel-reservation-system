package com.gharana.inventory_service.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import com.gharana.inventory_service.dto.AvailableRoomTypeDTO;

public interface InventoryService {
    List<AvailableRoomTypeDTO> queryRoomAvailability(Set<Long> hotelIds, LocalDate checkInDate, LocalDate checkOutDate);
}
