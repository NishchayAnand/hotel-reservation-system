package com.nivara.reservation_service.model.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ReservationItemDTO {
    private final Long roomTypeId;
    private final int qty;
    private final BigDecimal rate;
}
