package com.gharana.inventory_service.service;

import java.time.LocalDate;
import java.util.List;

public interface InventoryService {
    List<String> queryAvailability(List<String> hotelIds, LocalDate checkInDate, LocalDate checkOutDate);

}
