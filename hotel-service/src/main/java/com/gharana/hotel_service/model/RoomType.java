package com.gharana.hotel_service.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class RoomType {
    private String id;
    private String hotelId;
    private String name;
    private List<String> amenities;
}
