package com.gharana.hotel_service.model.mapper;

import com.gharana.hotel_service.model.dto.HotelDTO;
import com.gharana.hotel_service.model.entity.Hotel;

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
