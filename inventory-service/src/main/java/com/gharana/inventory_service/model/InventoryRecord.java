package com.gharana.inventory_service.model;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class InventoryRecord {
    private String hotelId;
    private String roomTypeId;
    private LocalDate date;
    private int totalRooms;
    private int reservedRooms;

    @Override
    public String toString() {
        return "InventoryRecord [hotelId=" + hotelId + ", roomTypeId=" + roomTypeId + ", date=" + date + ", totalRooms="
                + totalRooms + ", reservedRooms=" + reservedRooms + "]";
    }
}
