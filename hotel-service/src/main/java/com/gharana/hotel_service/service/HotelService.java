package com.gharana.hotel_service.service;

import com.gharana.hotel_service.entity.Hotel;

import java.util.List;

public interface HotelService {
    List<Hotel> getHotelsByLocationId(Long locationId);
}
