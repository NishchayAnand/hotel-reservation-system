package com.gharana.search_service.service;

import java.time.LocalDate;
import java.util.List;

import com.gharana.search_service.dto.AvailableHotelSummary;

public interface SearchService {
    List<AvailableHotelSummary> search(String destination, LocalDate checkInDate, LocalDate checkOutDate);
}
