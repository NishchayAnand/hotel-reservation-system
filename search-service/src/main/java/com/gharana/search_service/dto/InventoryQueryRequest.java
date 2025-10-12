package com.gharana.search_service.dto;

import java.time.LocalDate;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InventoryQueryRequest {
    private Set<Long> hotelIds;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
}
