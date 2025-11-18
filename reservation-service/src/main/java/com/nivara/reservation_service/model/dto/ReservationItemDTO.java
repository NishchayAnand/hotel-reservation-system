package com.nivara.reservation_service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ReservationItemDTO {
    Long roomTypeId;
    Integer quantity;
    Long rate;
}
