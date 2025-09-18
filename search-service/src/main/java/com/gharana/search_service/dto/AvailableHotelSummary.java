package com.gharana.search_service.dto;

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
    private int rating;
    private double lowestPrice;

    public AvailableHotelSummary(Hotel hotel) {
        this.hotelId = hotel.getId();
        this.hotelName = hotel.getName();
        this.address = hotel.getAddress();
        this.thumbnailURL = hotel.getThumbnailUrl();
        this.rating = hotel.getStarRating();
        this.lowestPrice = Integer.MAX_VALUE;
    }
}
