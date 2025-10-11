package com.gharana.search_service.service;

import java.time.LocalDate;
import java.util.List;

import com.gharana.search_service.dto.AvailableHotelDTO;

public interface SearchService {
    List<AvailableHotelDTO> search(Long locationId, LocalDate checkInDate, LocalDate checkOutDate);
}
