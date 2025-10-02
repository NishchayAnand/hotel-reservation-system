package com.gharana.hotel_service.entity;

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
    private String thumbnailUrl;
    private double customerRating;
    private int starRating;
    private List<String> amenities;
}
