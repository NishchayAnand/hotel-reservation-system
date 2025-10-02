package com.gharana.hotel_service.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gharana.hotel_service.dao.HotelRepository;
import com.gharana.hotel_service.dao.LocationRepository;
import com.gharana.hotel_service.dto.LocationDTO;
import com.gharana.hotel_service.model.Hotel;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class HotelServiceImpl implements HotelService { 

    private final LocationRepository locationRepository;
    private final HotelRepository hotelRepository;

	@Override
	public List<Hotel> getHotelsByDestination(LocationDTO location) {
		String locationId = locationRepository.getLocationId(location);
        List<Hotel> hotels = hotelRepository.getHotelsByLocationId(locationId);
        return hotels;
	}
    
}
