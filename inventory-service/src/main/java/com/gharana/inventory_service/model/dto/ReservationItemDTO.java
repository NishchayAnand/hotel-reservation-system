package com.gharana.inventory_service.model.dto;

import lombok.Data;

@Data
public class ReservationItemDTO {
    private final Long roomTypeId;
    private final int count;
}
