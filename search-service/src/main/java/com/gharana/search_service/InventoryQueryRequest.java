package com.gharana.search_service;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class InventoryQueryRequest {
    private List<String> hotelIds;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
}
