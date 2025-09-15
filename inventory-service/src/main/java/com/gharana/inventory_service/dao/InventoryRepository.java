package com.gharana.inventory_service.dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.gharana.inventory_service.model.InventoryRecord;

@Repository
public class InventoryRepository {

    public List<InventoryRecord> findByHotelIdsAndDateRange(List<String> hotelIds, LocalDate checkInDate, LocalDate checkOutDate) {
        // Will execute query to fetch inventory records from DB where hotelId in (hotelIds) and date between checkInDate and checkOutDate (excluded)
        return List.of(
            new InventoryRecord("1", "roomType1", LocalDate.now(), 10, 2),
            new InventoryRecord("1", "roomType2", LocalDate.now().plusDays(1), 5, 1)
        );
    }

}
