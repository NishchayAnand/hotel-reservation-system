package com.gharana.hotel_service.dao;

import org.springframework.stereotype.Repository;

import com.gharana.hotel_service.dto.LocationDTO;

@Repository
public class LocationRepository {

    public String getLocationId(LocationDTO location) {
        return null;

    }

    /*
     *         // Mock Implementation - replace with actual database call
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
     * 
    */

}
