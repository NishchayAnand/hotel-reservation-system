package com.gharana.hotel_service.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gharana.hotel_service.entity.RoomType;

public interface RoomTypeRepository extends JpaRepository<RoomType, Long> {
    List<RoomType> findByHotelIdAndIdIn(Long hotelId, Set<Long> roomTypeIds);
}
