package com.gharana.hotel_service.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gharana.hotel_service.dto.LocationDTO;
import com.gharana.hotel_service.entity.Hotel;
import com.gharana.hotel_service.entity.Location;
import com.gharana.hotel_service.repository.HotelRepository;
import com.gharana.hotel_service.repository.LocationRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class HotelServiceImpl implements HotelService { 

    private final LocationRepository locationRepository;
    private final HotelRepository hotelRepository;

	@Override
	public List<Hotel> getHotelsByDestination(LocationDTO location) {
		
        // Step 1: Find location Id for the specified location
        Long locationId = locationRepository
            .findFirstByCityIgnoreCaseAndStateIgnoreCaseAndCountryIgnoreCase(location.getCity(), location.getState(), location.getCountry())
            .map(Location::getId)
            .orElse(null);
        
        // Step 2: Find hotels in the specified location
        List<Hotel> hotels = hotelRepository.getHotelsByLocationId(locationId);
        
        return hotels;
	}
    
}
