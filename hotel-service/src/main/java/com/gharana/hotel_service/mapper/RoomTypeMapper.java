package com.gharana.hotel_service.mapper;

import com.gharana.hotel_service.dto.RoomTypeDTO;
import com.gharana.hotel_service.entity.RoomType;

public class RoomTypeMapper {

    public static RoomTypeDTO toDto(RoomType roomType) {
        return RoomTypeDTO.builder()
            .id(roomType.getId())
            .name(roomType.getName())
            .description(roomType.getDescription())
            .bedType(roomType.getBedType())
            .bedCount(roomType.getBedCount())
            .amenities(roomType.getAmenities())
            .build();
    }

}
