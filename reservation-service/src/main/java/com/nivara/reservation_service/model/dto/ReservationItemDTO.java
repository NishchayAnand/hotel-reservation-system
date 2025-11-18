package com.nivara.reservation_service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ReservationItemDTO {
    Long roomTypeId;
    String name;
    Integer quantity;
    Long rate;
}
