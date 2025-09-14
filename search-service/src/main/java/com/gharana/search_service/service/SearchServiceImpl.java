package com.gharana.search_service.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.gharana.search_service.client.HotelServiceClient;
import com.gharana.search_service.model.Hotel;

public class SearchServiceImpl implements SearchService {

    @Autowired
    private HotelServiceClient hotelServiceClient;

    public SearchServiceImpl(HotelServiceClient hotelServiceClient) {
        this.hotelServiceClient = hotelServiceClient;
    }

    @Override
    public List<Hotel> search(String destination, String checkInDate, String checkOutDate) {
        
        // -------------------------
        // Step 1: Query Hotel Service for hotel metadata
        // -------------------------
        List<Hotel> hotels = hotelServiceClient.getHotelsByDestination(destination);

        // -------------------------
        // Step 2: Query Inventory Service for availability
        // -------------------------

        return new ArrayList<>();
    }

}
