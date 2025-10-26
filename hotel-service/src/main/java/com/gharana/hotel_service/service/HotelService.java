package com.gharana.hotel_service.service;

import java.util.List;
import java.util.Set;

import com.gharana.hotel_service.model.dto.HotelDTO;
import com.gharana.hotel_service.model.dto.RoomTypeDTO;

public interface HotelService {
    List<HotelDTO> getHotelsByLocationId(Long locationId);
    HotelDTO getHotelById(Long hotelId);
    List<RoomTypeDTO> getRoomTypesByIds(Long hotelId, Set<Long> roomTypeIds);
}
