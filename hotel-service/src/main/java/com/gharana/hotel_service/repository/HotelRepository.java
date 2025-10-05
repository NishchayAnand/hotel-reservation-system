package com.gharana.hotel_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gharana.hotel_service.entity.Hotel;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long>  {

    // find hotels by the FK id of the associated Location entity
    List<Hotel> findByLocation_Id(Long locationId);


    /*
     * // Mock implementation - replace with actual database call
        List<Hotel> hotelMeta = List.of(
            new Hotel("101", "Grand Goa Resort", "A luxury seaside resort offering modern rooms, infinity pool, and beach access.", "GOA", "Candolim Beach Road, Goa, India", "https://cdn.example.com/101/thumb.jpg", 4.6, 4, List.of("wifi", "pool", "gym", "spa")),
            new Hotel("102", "Westin Resort", "Premium 5-star property featuring private beach, fine dining, and wellness spa.", "GOA", "Arpora Beach Road, Goa, India", "https://cdn.example.com/102/thumb.jpg", 4.7, 5, List.of("wifi", "pool", "private beach", "restaurant", "bar")),
            new Hotel("103", "Manali Mountain Inn", "Charming hill-side hotel with valley views and warm hospitality.", "MAN", "Old Manali, Manali, Himachal Pradesh", "https://cdn.example.com/103/thumb.jpg", 4.4, 3, List.of("wifi", "breakfast", "parking")),
            new Hotel("104", "Himalaya Comforts", "Comfortable rooms, bonfire area and guided trekking packages.", "MAN", "Mall Road, Manali, Himachal Pradesh", "https://cdn.example.com/104/thumb.jpg", 4.5, 4, List.of("wifi", "bonfire", "breakfast", "tour desk")),
            new Hotel("105", "Heritage Jaipur Palace", "Heritage property with traditional Rajasthani architecture near City Palace.", "JAIPUR", "Near City Palace, Jaipur, Rajasthan", "https://cdn.example.com/105/thumb.jpg", 4.8, 5, List.of("wifi", "spa", "restaurant", "parking")),
            new Hotel("106", "Pink City Stay", "Modern amenities with easy access to markets and monuments.", "JAI", "Bani Park, Jaipur, Rajasthan", "https://cdn.example.com/106/thumb.jpg", 4.2, 4, List.of("wifi", "breakfast", "airport transfer")),
            new Hotel("107", "Marine View Hotel", "Sea-facing rooms with rooftop restaurant and pool.", "MUM", "Marine Drive, Mumbai, Maharashtra", "https://cdn.example.com/107/thumb.jpg", 4.7, 5, List.of("wifi", "pool", "restaurant")),
            new Hotel("108", "City Center Lodge", "Business-friendly hotel in the heart of the city.", "MUM", "Fort, Mumbai, Maharashtra", "https://cdn.example.com/108/thumb.jpg", 4.1, 3, List.of("wifi", "meeting rooms", "breakfast")),
            new Hotel("109", "Lakeview Udaipur", "Romantic getaway with lake views and boat access.", "UDA", "Lake Pichola Road, Udaipur, Rajasthan", "https://cdn.example.com/109/thumb.jpg", 4.9, 5, List.of("wifi", "spa", "lake view", "restaurant")),
            new Hotel("110", "Budget Inn Udaipur", "Affordable rooms close to the old city attractions.", "UDA", "Old City, Udaipur, Rajasthan", "https://cdn.example.com/110/thumb.jpg", 4.0, 2, List.of("wifi", "breakfast"))
        );

        return hotelMeta.stream()
            .filter( hotel -> hotel.getLocationId().equals(locationId) )
            .toList();
     * 
     * 
     */

}
