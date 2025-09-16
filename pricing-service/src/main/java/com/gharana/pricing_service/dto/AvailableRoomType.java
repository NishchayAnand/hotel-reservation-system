package com.gharana.pricing_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class AvailableRoomType {
    private String hotelId;
    private String roomTypeId;
}
