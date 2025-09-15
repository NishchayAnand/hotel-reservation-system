package com.gharana.inventory_service.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gharana.inventory_service.dao.InventoryRepository;
import com.gharana.inventory_service.model.InventoryRecord;

@Service
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    public InventoryServiceImpl(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    public List<String> queryAvailability(List<String> hotelIds, LocalDate checkInDate, LocalDate checkOutDate) {
        List<InventoryRecord> records = inventoryRepository.findByHotelIdsAndDateRange(hotelIds, checkInDate, checkOutDate);
        return records.stream()
                      .map(InventoryRecord::getHotelId) // (record) -> record.getHotelId()
                      .distinct()
                      .toList();    
    }

}
