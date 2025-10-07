package com.gharana.inventory_service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoomTypeDTO {
    private String hotelId;
    private String roomTypeId;
}
