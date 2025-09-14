package com.gharana.inventory_service.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class InventoryQueryRequest {
    private List<String> hotelIds;
    private String checkInDate;
    private String checkOutDate;
}
