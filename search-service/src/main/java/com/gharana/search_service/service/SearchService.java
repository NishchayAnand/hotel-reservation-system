package com.gharana.search_service.service;

import java.time.LocalDate;
import java.util.List;

import com.gharana.search_service.dto.Hotel;
import com.gharana.search_service.dto.Location;

public interface SearchService {
    List<Hotel> search(Location location, LocalDate checkInDate, LocalDate checkOutDate);
}
