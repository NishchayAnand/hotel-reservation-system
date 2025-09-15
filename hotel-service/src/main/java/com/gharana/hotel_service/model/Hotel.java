package com.gharana.hotel_service.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor 
@Setter 
@Getter
public class Hotel {
    private String id;
    private String name;
    private String description;
    private String locationId;
    private String address;
    private int rating;
    private List<String> amenities;
}
