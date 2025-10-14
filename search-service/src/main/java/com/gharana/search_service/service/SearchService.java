package com.gharana.search_service.service;

import java.time.LocalDate;
import java.util.List;

import com.gharana.search_service.dto.AvailableHotelDTO;
import com.gharana.search_service.dto.AvailableRoomTypeDTO;

public interface SearchService {
    List<AvailableHotelDTO> getAvailableHotelsByLocation(Long locationId, LocalDate checkInDate, LocalDate checkOutDate);
    List<AvailableRoomTypeDTO> getAvailableRoomTypesByHotel(Long hotelId, LocalDate checkInDate, LocalDate checkOutDate);
}
