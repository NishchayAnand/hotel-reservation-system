package com.gharana.search_service.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.gharana.search_service.client.HotelServiceClient;
import com.gharana.search_service.client.InventoryServiceClient;
import com.gharana.search_service.client.PricingServiceClient;
import com.gharana.search_service.dto.AvailableRoomTypeDTO;
import com.gharana.search_service.dto.HotelDTO;
import com.gharana.search_service.dto.RoomAvailabilityRequest;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SearchServiceImpl implements SearchService {

    private final HotelServiceClient hotelServiceClient;
    private final InventoryServiceClient inventoryServiceClient;
    private final PricingServiceClient pricingServiceClient;

    @Override
    public List<HotelDTO> search(Long locationId, LocalDate checkInDate, LocalDate checkOutDate) {

        List<HotelDTO> availableHotels = new ArrayList<>();
        
        // Step 1: Query Hotel Service for hotel metadata
        List<HotelDTO> hotels = hotelServiceClient.getHotelsByLocationId(locationId);
        if(hotels.isEmpty()) return List.of();

        // Step 2: Query Inventory Service to get list of room types which have rooms available on every date in the [chekInDate, checkOutDate) date range.
        List<String> hotelIds = hotels.stream().map(HotelDTO::getId).toList();
        List<AvailableRoomTypeDTO> availableRoomTypes = inventoryServiceClient
            .queryRoomAvailability(new RoomAvailabilityRequest(hotelIds, checkInDate, checkOutDate));
        
        // Step 3: 
        // Step 3: for each availableRoomType, we would the avg price per night - suggest the best flow to get this detail considering we have a pricingService which interacts with the pricing
        /* 
        
        // List<PricingRecord> pricingRecords = pricingServiceClient.getPricingByRoomTypes(List<AvailableRoomTypes>, checkInDate, checkOutDate);

        // Step 3: Build a map of Hotel metadata for easy lookup
        Map<String, Hotel> hotelById = hotels.stream().collect(Collectors.toMap(Hotel::getId, hotelObj -> hotelObj));

        // Step 4: Group available room types by hotelId
        Map<String, List<AvailableRoomType>> grouped = availableRoomTypes.stream()
            .collect(Collectors.groupingBy(AvailableRoomType::getHotelId));

        // Step 5: For each available hotel, find the lowest per-night average price across among all available room types
        for(String hotelId : grouped.keySet()) {
            Hotel hotel = hotelById.get(hotelId);
            for(AvailableRoomType availableRoomType : grouped.get(hotelId) ) {
                double price = pricingServiceClient.getAvgPricePerNight(availableRoomType.getHotelId(), availableRoomType.getRoomTypeId(), checkInDate, checkOutDate);
                //hotel.setLowestPrice(Math.min(hotel.getAvg(), price));
            }
            availableHotels.add(hotel);
        }

        return availableHotels;
        */
        return hotels;
    }

}
