package com.gharana.search_service.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class AvailableHotelSummary {
    private String hotelId;
    private String hotelName;
    private String address;
    private String thumbnailURL;
    private double rating;
    private int starRating;
    private List<String> amenities;
    private double lowestPrice;
}
