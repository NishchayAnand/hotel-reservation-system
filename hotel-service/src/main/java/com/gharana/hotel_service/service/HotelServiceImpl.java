package com.gharana.hotel_service.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.gharana.hotel_service.dao.HotelRepository;
import com.gharana.hotel_service.dao.LocationRepository;
import com.gharana.hotel_service.model.Hotel;

public class HotelServiceImpl implements HotelService { 

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private HotelRepository hotelRepository;  

    public HotelServiceImpl(LocationRepository locationRepository, HotelRepository hotelRepository) {
        this.locationRepository = locationRepository;
        this.hotelRepository = hotelRepository;
    }

	@Override
	public List<Hotel> getHotelsByDestination(String destination) {
		String locationId = locationRepository.getLocationId(destination);
        List<Hotel> hotels = hotelRepository.getHotelsByLocationId(locationId);
        return hotels;
	}
}
