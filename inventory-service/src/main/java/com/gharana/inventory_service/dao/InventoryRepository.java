package com.gharana.inventory_service.dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.gharana.inventory_service.model.InventoryRecord;

@Repository
public class InventoryRepository {

    public List<InventoryRecord> findAvailableRoomTypeByHotelIdAndDateRange(List<String> hotelIds, LocalDate checkInDate, LocalDate checkOutDate) {

        List<InventoryRecord> inventoryRecords = List.of(
            new InventoryRecord("101", "roomType1", LocalDate.of(2025, 9, 18), 11, 3),
            new InventoryRecord("101", "roomType1", LocalDate.of(2025, 9, 19), 11, 4), 
            new InventoryRecord("101", "roomType2", LocalDate.of(2025, 9, 18), 7, 7), 
            new InventoryRecord("101", "roomType2", LocalDate.of(2025, 9, 19), 7, 3), 
            new InventoryRecord("102", "roomType1", LocalDate.of(2025, 9, 18), 10, 10), 
            new InventoryRecord("102", "roomType1", LocalDate.of(2025, 9, 19), 10, 10), 
            new InventoryRecord("102", "roomType2", LocalDate.of(2025, 9, 18), 5, 1), 
            new InventoryRecord("102", "roomType2", LocalDate.of(2025, 9, 19),5, 2)
        );
        
        return inventoryRecords.stream()
            .filter(record -> hotelIds.contains(record.getHotelId()))
            .filter(record -> !record.getDate().isBefore(checkInDate) && record.getDate().isBefore(checkOutDate))
            .toList();

    }

}
