package com.gharana.pricing_service.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class PricingQueryRequestDTO {
    private List<AvailableRoomTypeDTO> availableRoomTypes;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
}
