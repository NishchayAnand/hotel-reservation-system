package com.gharana.search_service.service;

import java.time.LocalDate;
import java.util.List;

import com.gharana.search_service.dto.Hotel;

public interface SearchService {
    List<Hotel> search(String destination, LocalDate checkInDate, LocalDate checkOutDate);
}
