package com.gharana.search_service.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.gharana.search_service.InventoryQueryRequest;
import com.gharana.search_service.client.HotelServiceClient;
import com.gharana.search_service.client.InventoryServiceClient;
import com.gharana.search_service.dto.RoomTypeRate;
import com.gharana.search_service.model.Hotel;

public class SearchServiceImpl implements SearchService {

    @Autowired
    private HotelServiceClient hotelServiceClient;

    @Autowired
    private InventoryServiceClient inventoryServiceClient;

    public SearchServiceImpl(HotelServiceClient hotelServiceClient, 
                                InventoryServiceClient inventoryServiceClient) {
        this.hotelServiceClient = hotelServiceClient;
        this.inventoryServiceClient = inventoryServiceClient;
    }

    @Override
    public List<Hotel> search(String destination, LocalDate checkInDate, LocalDate checkOutDate) {
        
        // -------------------------
        // Step 1: Query Hotel Service for hotel metadata
        // -------------------------
        List<Hotel> hotels = hotelServiceClient.getHotelsByDestination(destination);

        // -------------------------
        // Step 2: Query Inventory Service for availability
        // -------------------------
        List<String> hotelIds = hotels.stream()
                                      .map(Hotel::getId) // (hotel) -> hotel.getId()
                                      .toList();
        List<String> availableHotelIds = inventoryServiceClient.queryAvailability(new InventoryQueryRequest(hotelIds, checkInDate, checkOutDate));

        // -------------------------
        // Step 3: Query Pricing Service for rates per day of all available room types 
        // -------------------------
        <String, List<RoomTypeRate> pricingMap = rateServiceClient.getRoomRates(availableHotelIds, checkInDate, checkOutDate);    

        // -------------------------
        // Step 4: Filter hotels based on availability
        // -------------------------
        List<Hotel> availableHotels = new ArrayList<>();
        for (Hotel hotel : hotels) {
            if (availableHotelIds.contains(hotel.getId())) {    // only optimize the search to use binary search if the list is sorted
                availableHotels.add(hotel);
            }
        }
   
        return new ArrayList<>();
    }

}
