package com.gharana.search_service.service;

import java.util.ArrayList;
import java.util.List;

import com.gharana.search_service.model.Hotel;

public class SearchServiceImpl implements SearchService {

    @Override
    public List<Hotel> search(String destination, String checkInDate, String checkOutDate) {
        
        // -------------------------
        // Step 1: Fetch hotel metadata
        // -------------------------
        List<Hotel> hotels = HotelAPI.getHotelsByDestination(destination);

        return new ArrayList<>();
    }

}
