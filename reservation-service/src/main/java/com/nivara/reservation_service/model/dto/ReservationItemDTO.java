package com.nivara.reservation_service.model.dto;

public record ReservationItemDTO(
    Long roomTypeId, 
    Integer quantity, 
    Long rate
) {}
