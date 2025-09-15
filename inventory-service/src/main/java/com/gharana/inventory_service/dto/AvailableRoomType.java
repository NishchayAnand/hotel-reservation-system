package com.gharana.inventory_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class AvailableRoomType {
    private String hotelId;
    private String roomTypeId;
}
