package com.gharana.inventory_service.model.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class HoldInventoryRequestDTO {
    private final Long hotel_id;
    private final LocalDate checkInDate;
    private final LocalDate checkOutDate;
    private List<SelectedRoomTypeInventoryDTO> selectedRooms;
}
