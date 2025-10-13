package com.gharana.search_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InventoryRecordDTO {
    private Long id;
    private Long hotelId;
    private Long roomTypeId;
    private int totalCount;
    private int reservedCount;
}
