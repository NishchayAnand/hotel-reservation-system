package com.gharana.inventory_service.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gharana.inventory_service.dto.RoomTypeDTO;
import com.gharana.inventory_service.model.InventoryRecord;
import com.gharana.inventory_service.repository.InventoryRepository;

@Service
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    public InventoryServiceImpl(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    public List<RoomTypeDTO> queryRoomAvailability(List<String> hotelIds, LocalDate checkInDate, LocalDate checkOutDate) { 
        // Step 1: Fetch inventory records for the given hotels that fall within the [checkInDate, checkOutDate) range  
        List<InventoryRecord> records = inventoryRepository.findAvailableRoomTypesForHotels(hotelIds, checkInDate, checkOutDate);
        return records.stream()
            .map(this::toRoomTypeDTO)
            .collect(Collectors.toList()); 
    }

    private RoomTypeDTO toRoomTypeDTO(InventoryRecord record) {
        return RoomTypeDTO.builder()
            .hotelId(record.getHotelId())
            .roomTypeId(record.getRoomTypeId())
            .build();
    }

    /*


        List<RoomTypeDTO> availableRoomTypes = new ArrayList<>();
     * 
     * // Step 2: Group records by hotelId and roomTypeId
        Map<String, Map<String, List<InventoryRecord>>> grouped = records.stream()
            .collect(Collectors.groupingBy(InventoryRecord::getHotelId, 
                     Collectors.groupingBy(InventoryRecord::getRoomTypeId)));

        // Step 3: For each hotelId and roomTypeId, check if rooms are available for every day in the range [checkInDate, checkOutDate)
        long days = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        for(String hotelId : grouped.keySet()) {
            Map<String, List<InventoryRecord>> byRoomType = grouped.get(hotelId);
            for(String roomTypeId : byRoomType.keySet()) {
                List<InventoryRecord> roomTypeRecords = byRoomType.get(roomTypeId);
                if(roomTypeRecords.size() == days) {
                    availableRoomTypes.add(new AvailableRoomType(hotelId, roomTypeId));
                }
            }
        }

        return availableRoomTypes;  
     * 
     * 
     * 
    */

}
