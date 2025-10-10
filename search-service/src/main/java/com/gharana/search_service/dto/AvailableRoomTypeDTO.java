package com.gharana.search_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AvailableRoomTypeDTO {
    private String hotelId;
    private String roomTypeId;
}
