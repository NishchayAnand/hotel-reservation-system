package com.gharana.hotel_service.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.gharana.hotel_service.model.Hotel;

@Repository
public class HotelRepository {

    public List<Hotel> getHotelsByLocationId(String locationId) {
        // Mock implementation - replace with actual database call
        return List.of(
            new Hotel("1", "Hotel A", locationId, "Address A", 4.5, 5, List.of("Free WiFi", "Pool")),
            new Hotel("2", "Hotel B", locationId, "Address B", 4.0, 4, List.of("Free Breakfast", "Gym"))
        );
    }

}
