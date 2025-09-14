package com.gharana.inventory_service.service;

import java.util.List;

import com.gharana.inventory_service.model.InventoryRecord;

public interface InventoryService {
    List<InventoryRecord> queryAvailability(List<String> hotelIds, String checkInDate, String checkOutDate);

}
