package com.gharana.hotel_service.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gharana.hotel_service.dto.HotelDTO;
import com.gharana.hotel_service.entity.Hotel;
import com.gharana.hotel_service.entity.Location;
import com.gharana.hotel_service.repository.LocationRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class HotelServiceImpl implements HotelService { 

    private final LocationRepository locationRepository;

	@Override
    @Transactional(readOnly = true)
	public List<HotelDTO> getHotelsByLocation(String city, String state, String country) {
		
        // Step 1: Find location Id for the specified location
        Location location = locationRepository
            .findFirstByCityAndStateAndCountry(city, state, country)
            .orElse(null);

        if(location == null) return List.of();
        
        // Step 2: Find hotels in the specified location
        List<Hotel> hotels = location.getHotels();
        
        return hotels.stream()
            .map(this::toDto)
            .collect(Collectors.toList());
	}

    private HotelDTO toDto(Hotel hotel) {
        return HotelDTO.builder()
            .id(hotel.getId())
            .name(hotel.getName())
            .address(hotel.getAddress())
            .description(hotel.getDescription())
            .thumbnailUrl(hotel.getThumbnailUrl())
            .rating(hotel.getRating())
            .build();

    }
    
}
