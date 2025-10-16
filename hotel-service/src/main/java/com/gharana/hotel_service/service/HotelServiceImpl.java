package com.gharana.hotel_service.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.gharana.hotel_service.dto.HotelDTO;
import com.gharana.hotel_service.dto.RoomTypeDTO;
import com.gharana.hotel_service.mapper.HotelMapper;
import com.gharana.hotel_service.mapper.RoomTypeMapper;
import com.gharana.hotel_service.repository.HotelRepository;
import com.gharana.hotel_service.repository.RoomTypeRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class HotelServiceImpl implements HotelService { 

    private final HotelRepository hotelRepository;
	private final RoomTypeRepository roomTypeRepository;

	@Override
	public List<HotelDTO> getHotelsByLocationId(Long locationId) {  
        return hotelRepository.findByLocationId(locationId).stream()
			.map(HotelMapper::toDto)
			.collect(Collectors.toList());
	}

	@Override
	public HotelDTO getHotelById(Long hotelId) {
		return hotelRepository.findById(hotelId)
			.map(HotelMapper::toDto)
			.orElse(null);
	}

	@Override
	public List<RoomTypeDTO> getRoomTypesByIds(Long hotelId, Set<Long> roomTypeIds) {
		
		List<RoomTypeDTO> results = roomTypeRepository.findByHotelIdAndIdIn(hotelId, roomTypeIds).stream()
			.map(RoomTypeMapper::toDto)
			.collect(Collectors.toList());
		
		return results;
		
	}
    
}
