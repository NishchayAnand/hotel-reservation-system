package com.gharana.search_service.service;

import java.util.List;

import com.gharana.search_service.model.Hotel;

public interface SearchService {
    List<Hotel> search(String destination, String checkInDate, String checkOutDate);
}
