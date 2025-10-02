package com.gharana.hotel_service.service;

import com.gharana.hotel_service.dto.LocationDTO;
import com.gharana.hotel_service.model.Hotel;

import java.util.List;

public interface HotelService {
    List<Hotel> getHotelsByDestination(LocationDTO location);
}
