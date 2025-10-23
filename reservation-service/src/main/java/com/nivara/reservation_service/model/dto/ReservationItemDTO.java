package com.nivara.reservation_service.dto;

import lombok.Data;

@Data
public class ReservationItemDTO {
    private final Long roomTypeId;
    private final int qty;
}
