package com.gharana.hotel_service.service;

import com.gharana.hotel_service.dto.HotelDTO;
import com.gharana.hotel_service.dto.RoomTypeDTO;

import java.util.List;
import java.util.Set;

public interface HotelService {
    List<HotelDTO> getHotelsByLocationId(Long locationId);
    HotelDTO getHotelById(Long hotelId);
    List<RoomTypeDTO> getRoomTypesByIds(Set<Long> roomTypeIds);
}
