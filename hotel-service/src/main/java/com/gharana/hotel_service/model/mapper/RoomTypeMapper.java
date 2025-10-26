package com.gharana.hotel_service.model.mapper;

import com.gharana.hotel_service.model.dto.RoomTypeDTO;
import com.gharana.hotel_service.model.entity.RoomType;

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
