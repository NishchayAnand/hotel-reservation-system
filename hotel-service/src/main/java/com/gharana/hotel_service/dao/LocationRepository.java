package com.gharana.hotel_service.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.gharana.hotel_service.model.Location;

@Repository
public class LocationRepository {

    public String getLocationId(String destination) {
        // Mock Implementation - replace with actual database call
         List<Location> locationMeta = List.of(
            new Location("GOA", "Goa"),
            new Location("MAN", "Manali"),
            new Location("JAI", "Jaipur"),
            new Location("MUM", "Mumbai"),
            new Location("UDA", "Udaipur")
        );

        return locationMeta.stream()
        .filter(location -> location.getDestination().equals(destination))
        .map(Location::getLocationId)
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Destination not found: " + destination));
    }


}
