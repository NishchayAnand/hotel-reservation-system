package com.gharana.inventory_service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AvailableRoomTypeDTO {
    private Long hotelId;
    private Long roomTypeId;
    private int availableRoomCount;
}
