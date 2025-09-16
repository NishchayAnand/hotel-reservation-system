package com.gharana.search_service.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.gharana.search_service.client.HotelServiceClient;
import com.gharana.search_service.client.InventoryServiceClient;
import com.gharana.search_service.client.PricingServiceClient;
import com.gharana.search_service.dto.AvailableHotelSummary;
import com.gharana.search_service.dto.AvailableRoomType;
import com.gharana.search_service.dto.InventoryQueryRequest;
import com.gharana.search_service.dto.PricingQueryRequest;
import com.gharana.search_service.model.Hotel;

public class SearchServiceImpl implements SearchService {

    @Autowired
    private HotelServiceClient hotelServiceClient;

    @Autowired
    private InventoryServiceClient inventoryServiceClient;

    @Autowired 
    PricingServiceClient pricingServiceClient;

    public SearchServiceImpl(HotelServiceClient hotelServiceClient, 
                                InventoryServiceClient inventoryServiceClient,
                                PricingServiceClient pricingServiceClient) {
        this.hotelServiceClient = hotelServiceClient;
        this.inventoryServiceClient = inventoryServiceClient;
        this.pricingServiceClient = pricingServiceClient;
    }

    @Override
    public List<AvailableHotelSummary> search(String destination, LocalDate checkInDate, LocalDate checkOutDate) {

        List<AvailableHotelSummary> availableHotelSummaries = new ArrayList<>();
        
        // Step 1: Query Hotel Service for hotel metadata
        List<Hotel> hotels = hotelServiceClient.getHotelsByDestination(destination);

        // Step 2: Query Inventory Service to get list of room types which have rooms available on every date in the [chekInDate, checkOutDate) date range.
        List<String> hotelIds = hotels.stream().map(Hotel::getId).toList();
        List<AvailableRoomType> availableRoomTypes = inventoryServiceClient
            .getAvailableRoomTypes(new InventoryQueryRequest(hotelIds, checkInDate, checkOutDate));

        // List<PricingRecord> pricingRecords = pricingServiceClient.getPricingByRoomTypes(List<AvailableRoomTypes>, checkInDate, checkOutDate);

        // Step 3: Build a map of Hotel metadata for easy lookup
        Map<String, Hotel> hotelById = hotels.stream().collect(Collectors.toMap(Hotel::getId, hotelObj -> hotelObj));

        // Step 4: Group available room types by hotelId
        Map<String, List<AvailableRoomType>> grouped = availableRoomTypes.stream()
            .collect(Collectors.groupingBy(AvailableRoomType::getHotelId));

        // Step 5: For each available hotel, find the lowest price across the date range: [checkInDate, checkOutDate) among all available room types
        for(String hotelId : grouped.keySet()) {
            Hotel hotel = hotelById.get(hotelId);
            AvailableHotelSummary hotelSummary = new AvailableHotelSummary(hotel);
            for(AvailableRoomType availableRoomType : grouped.get(hotelId) ) {
                List<Double> priceList = pricingServiceClient
                    .getPrice(new PricingQueryRequest(availableRoomType.getHotelId(), availableRoomType.getRoomTypeId(), checkInDate, checkOutDate));
                double minPrice = Collections.min(priceList);
                hotelSummary.setLowestPrice(Math.min(hotelSummary.getLowestPrice(), minPrice));
            }
            availableHotelSummaries.add(hotelSummary);
        }

        return availableHotelSummaries;
    }

}
