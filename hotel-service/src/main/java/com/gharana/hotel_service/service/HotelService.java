package com.gharana.hotel_service.service;

import com.gharana.hotel_service.dto.HotelDTO;

import java.util.List;

public interface HotelService {
    List<HotelDTO> getHotelsByLocation(Long locationId);
}
