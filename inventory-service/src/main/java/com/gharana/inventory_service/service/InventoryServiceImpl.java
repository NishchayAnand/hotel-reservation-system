package com.gharana.inventory_service.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.gharana.inventory_service.model.dto.AvailableRoomTypeDTO;
import com.gharana.inventory_service.model.dto.HoldDTO;
import com.gharana.inventory_service.model.dto.SelectedRoomTypeInventoryDTO;
import com.gharana.inventory_service.model.entity.Hold;
import com.gharana.inventory_service.repository.HoldRepository;
import com.gharana.inventory_service.repository.InventoryRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private InventoryRepository inventoryRepository;
    private HoldRepository holdRepository;

    @Override
    public List<AvailableRoomTypeDTO> getAvailableRoomTypes(Set<Long> hotelIds, LocalDate checkInDate, LocalDate checkOutDate) { 
        // Fetch room types for the given hotels that have availability across the entire [checkInDate, checkOutDate) range.  
        long nights = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        List<Object[]> rows = inventoryRepository.findAvailableRoomTypesForHotels(hotelIds, checkInDate, checkOutDate, nights); 
        return rows.stream()
            .map(row -> new AvailableRoomTypeDTO( // row[0] => hotelId, row[1] => roomTypeId, row[2] => availableRoomCount
                (Long) row[0], 
                (Long) row[1],
                (int) row[2])) 
            .collect(Collectors.toList());
    }

    @Override
    public HoldDTO createInventoryHold(String requestId, Long hotelId, LocalDate checkInDate, LocalDate checkOutDate,
            List<SelectedRoomTypeInventoryDTO> selectedRooms) {

        for(SelectedRoomTypeInventoryDTO selection: selectedRooms) {
            String holdId = requestId + "-" + selection.getRoomTypeId(); // allows multi-line holds per request
            
            Optional<Hold> existing = holdRepository.findByHoldId(holdId);
            if(existing.isPresent()) {
                continue;
            }
            

        }
        
        return null;

    }

}
