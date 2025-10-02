package com.gharana.hotel_service.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Room {
    private String id;
    private String roomTypeId;
    private String hotelId;
    private String name;
    private int number;
    private List<String> amenities;
}
