package com.gharana.search_service.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.gharana.search_service.InventoryQueryRequest;
import com.gharana.search_service.client.HotelServiceClient;
import com.gharana.search_service.client.InventoryServiceClient;
import com.gharana.search_service.dto.AvailableHotelSummary;
import com.gharana.search_service.dto.AvailableRoomType;
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
    public List<AvailableHotelSummary> search(String destination, LocalDate checkInDate, LocalDate checkOutDate) {
        
        // Step 1: Query Hotel Service for hotel metadata
        List<Hotel> hotels = hotelServiceClient.getHotelsByDestination(destination);

        // Step 2: Query Inventory Service to get list of room types which have rooms available on every date in the [chekInDate, checkOutDate) date range.
        List<String> hotelIds = hotels.stream()
                                      .map(Hotel::getId) // (hotel) -> hotel.getId()
                                      .toList();
        List<AvailableRoomType> availableRoomTypes = inventoryServiceClient.getAvailableRoomTypes(new InventoryQueryRequest(hotelIds, checkInDate, checkOutDate));

        // Step 3: For available hotels, compute the lowest price across room types for the date range
        List<AvailableHotelSummary> availableHotelSummaries = new ArrayList<>();
        
        return new ArrayList<>();
    }

}
