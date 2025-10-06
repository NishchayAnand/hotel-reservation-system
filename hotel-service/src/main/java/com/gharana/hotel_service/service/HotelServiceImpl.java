package com.gharana.hotel_service.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gharana.hotel_service.entity.Hotel;
import com.gharana.hotel_service.repository.HotelRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class HotelServiceImpl implements HotelService { 

    private final HotelRepository hotelRepository;

	@Override
	public List<Hotel> getHotelsByLocationId(Long locationId) {  
        return hotelRepository.findByLocationId(locationId);
	}
    
}
