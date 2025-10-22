package com.nivara.reservation_service.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ReservationItem {
    private final Long roomTypeId;
    private final int qty;
    private final BigDecimal rate;
    private final BigDecimal subtotal;
}
