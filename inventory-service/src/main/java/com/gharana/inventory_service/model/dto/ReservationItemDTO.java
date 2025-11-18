package com.gharana.inventory_service.model.dto;

public record ReservationItemDTO (
    Long roomTypeId,
    String name, 
    Integer quantity,
    Long rate) {}
