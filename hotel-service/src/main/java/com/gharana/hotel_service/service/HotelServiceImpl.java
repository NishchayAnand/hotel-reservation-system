package com.gharana.hotel_service.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.gharana.hotel_service.dto.HotelDTO;
import com.gharana.hotel_service.entity.Hotel;
import com.gharana.hotel_service.repository.HotelRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class HotelServiceImpl implements HotelService { 

    private final HotelRepository hotelRepository;

	@Override
	public List<HotelDTO> getHotelsByLocation(Long locationId) {  
        return hotelRepository.findByLocationId(locationId).stream()
			.map(this::toDto)
			.collect(Collectors.toList());
	}

	private HotelDTO toDto(Hotel hotel) {
		return HotelDTO.builder()
			.id(hotel.getId())
			.name(hotel.getName())
			.address(hotel.getAddress())
			.shortDescription(hotel.getShortDescription())
			.longDescription(hotel.getLongDescription())
			.thumbnailUrl(hotel.getThumbnailUrl())
			.rating(hotel.getRating())
			.amenities(hotel.getAmenities())
			.build();	
	}
    
}
