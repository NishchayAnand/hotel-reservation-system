package com.gharana.inventory_service.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.gharana.inventory_service.model.InventoryRecord;


public interface InventoryRepository extends JpaRepository<InventoryRecord, Long> {

    @Query("")
    public List<InventoryRecord> findAvailableRoomTypesForHotels(List<String> hotelIds, LocalDate checkInDate, LocalDate checkOutDate);

}
