package com.gharana.hotel_service.dao;

public class LocationRepository {

    public String getLocationId(String destination) {
        // Mock Implementation - replace with actual database call
        switch(destination.toLowerCase()) {
            case "manali":
                return "MAN";
            case "delhi":
                return "DEL";
            case "mumbai":
                return "MUM";
            default:
                return "UNKNOWN";
        }
    }


}
