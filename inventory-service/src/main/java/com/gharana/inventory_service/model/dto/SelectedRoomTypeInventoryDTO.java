package com.gharana.inventory_service.model.dto;

import lombok.Data;

@Data
public class SelectedRoomTypeInventoryDTO {
    private final Long roomTypeId;
    private final int qty;
}
