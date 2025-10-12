package com.gharana.inventory_service.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.gharana.inventory_service.dto.AvailableRoomTypeDTO;
import com.gharana.inventory_service.repository.InventoryRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private InventoryRepository inventoryRepository;

    @Override
    public List<AvailableRoomTypeDTO> queryRoomAvailability(Set<Long> hotelIds, LocalDate checkInDate, LocalDate checkOutDate) { 
        // Fetch room types for the given hotels that have availability across the entire [checkInDate, checkOutDate) range.  
        long nights = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        List<Object[]> rows = inventoryRepository.findAvailableRoomTypesForHotels(hotelIds, checkInDate, checkOutDate, nights);
        return rows.stream()
            .map(row -> new AvailableRoomTypeDTO(
                (Long) row[0], 
                (Long) row[1])) // row[0] => hotelId, row[1] => roomTypeId
            .collect(Collectors.toList());
    }

}
