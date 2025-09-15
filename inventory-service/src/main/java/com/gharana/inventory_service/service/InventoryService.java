package com.gharana.inventory_service.service;

import java.time.LocalDate;
import java.util.List;

import com.gharana.inventory_service.dto.AvailableRoomType;

public interface InventoryService {
    List<AvailableRoomType> getAvailableRoomTypes(List<String> hotelIds, LocalDate checkInDate, LocalDate checkOutDate);

}
