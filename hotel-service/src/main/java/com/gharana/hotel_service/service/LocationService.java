package com.gharana.hotel_service.service;

import java.util.List;

import com.gharana.hotel_service.model.dto.LocationDTO;

public interface LocationService {
    
    List<LocationDTO> getAllLocations();

}
