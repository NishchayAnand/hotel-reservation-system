package com.gharana.hotel_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gharana.hotel_service.entity.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {

    Optional<Location> findFirstByCityAndStateAndCountry(String city, String State, String country);

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
