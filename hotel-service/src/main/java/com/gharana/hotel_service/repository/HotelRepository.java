package com.gharana.hotel_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gharana.hotel_service.model.entity.Hotel;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long>  {
    List<Hotel> findByLocationId(Long locationId);
}
