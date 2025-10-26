package com.gharana.inventory_service.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.gharana.inventory_service.model.entity.InventoryRecord;
import com.gharana.inventory_service.model.dto.AvailableRoomTypeDTO;
import com.gharana.inventory_service.model.dto.HoldDTO;
import com.gharana.inventory_service.model.dto.SelectedInventoryDTO;
import com.gharana.inventory_service.model.entity.Hold;
import com.gharana.inventory_service.repository.HoldRepository;
import com.gharana.inventory_service.repository.InventoryRepository;

import jakarta.transaction.Transactional;
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
    @Transactional
    public HoldDTO createInventoryHold(String requestId, 
        Long hotelId, 
        LocalDate checkInDate, 
        LocalDate checkOutDate,
        List<SelectedInventoryDTO> selectedInventory) {

        // idempotent check: if an ACTIVE hold exists for this requestId, return it
        Optional<Hold> existing = holdRepository.findByRequestId(requestId);
        if(existing.isPresent()) {
            Hold hold = existing.get();
            if(hold.getStatus().toString().equalsIgnoreCase("ACTIVE")) {
                return HoldDTO.builder()
                    .holdId(hold.getId())
                    .success(true)
                    .created(false)
                    .expiresAt(hold.getExpiresAt())
                    .message("existing active hold")
                    .build();
            }
            // if exists but not ACTIVE, can attempt new hold, if required.
        }

        // pre-check & lock inventory rows for every selected room type
        for(SelectedInventoryDTO selection: selectedInventory) {
            List<InventoryRecord> records = inventoryRepository.findByHotelIdAndRoomTypeIdAndReservationDateBetween(
                hotelId, 
                selection.getRoomTypeId(), 
                checkInDate, 
                checkInDate.minusDays(1));

            // do availability check to ensure the selected quantity is still available
            for(InventoryRecord record: records) {
                int availableRooms = record.getTotalCount() - record.getReservedCount();
                if(availableRooms < selection.getQty()) {
                    return HoldDTO.builder()
                        .success(false)
                        .message("insufficient inventory for roomType:" + selection.getRoomTypeId())
                        .build();
                }
            }


        }





        return null;

    }

}
