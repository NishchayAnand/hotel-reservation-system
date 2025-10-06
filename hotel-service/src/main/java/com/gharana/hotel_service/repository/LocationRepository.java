package com.gharana.hotel_service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gharana.hotel_service.entity.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {
    Optional<Location> findFirstByCity(String city);
    List<Location> findAll();
}
