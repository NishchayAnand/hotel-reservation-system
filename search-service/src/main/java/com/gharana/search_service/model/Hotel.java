package com.gharana.search_service.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Hotel {
    private String id;
    private String name;
    private String locationId;
    private String address;
    private String thumbnailURL;
    private double rating;
    private int starRating;
    private List<String> amenities;
}
