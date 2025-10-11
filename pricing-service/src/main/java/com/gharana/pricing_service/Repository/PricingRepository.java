package com.gharana.pricing_service.Repository;

import java.time.LocalDate;
import java.util.List;

import com.gharana.pricing_service.dto.AvailableRoomTypeDTO;

public interface PricingRepository {
    public List<Object[]> getAvgRateByHotelAndRoomType(List<AvailableRoomTypeDTO> availableRoomTypes, LocalDate checkInDate, LocalDate checkOutDate);
}
