package com.gharana.hotel_service.mapper;

import com.gharana.hotel_service.dto.HotelDTO;
import com.gharana.hotel_service.entity.Hotel;

public class HotelMapper {

    public static HotelDTO toDto(Hotel hotel) {
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
