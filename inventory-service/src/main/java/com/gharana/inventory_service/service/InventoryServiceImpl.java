package com.gharana.inventory_service.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.stereotype.Service;

import com.gharana.inventory_service.dto.AvailableRoomTypeDTO;
import com.gharana.inventory_service.repository.InventoryRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private InventoryRepository inventoryRepository;

    @Override
    public List<AvailableRoomTypeDTO> queryRoomAvailability(List<String> hotelIds, LocalDate checkInDate, LocalDate checkOutDate) { 
        // Step 1: Fetch inventory records for the given hotels that fall within the [checkInDate, checkOutDate) range  
        long nights = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        return inventoryRepository.findAvailableRoomTypesForHotels(hotelIds, checkInDate, checkOutDate, nights);
    }

}
