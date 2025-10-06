package com.gharana.search_service.service;

import java.time.LocalDate;
import java.util.List;

import com.gharana.search_service.dto.HotelDTO;

public interface SearchService {
    List<HotelDTO> search(Long locationId, LocalDate checkInDate, LocalDate checkOutDate);
}
