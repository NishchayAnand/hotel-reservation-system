package com.gharana.search_service.dto;

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
    private String description;
    private String locationId;
    private String address;
    private String thumbnailUrl;
    private String rating;
    private List<String> amenities;
}
