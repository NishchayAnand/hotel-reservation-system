package com.gharana.hotel_service.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.gharana.hotel_service.model.dto.LocationDTO;
import com.gharana.hotel_service.model.entity.Location;
import com.gharana.hotel_service.repository.LocationRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class LocationServiceImpl implements LocationService{

    private final LocationRepository locationRepository;

    @Override
    public List<LocationDTO> getAllLocations() {
        return locationRepository.findAll().stream()
            .map(this::toDto)
            .collect(Collectors.toList());  
    }

    private LocationDTO toDto(Location location) {
        return LocationDTO.builder()
            .id(location.getId())
            .city(location.getCity())
            .build();
    }

}
