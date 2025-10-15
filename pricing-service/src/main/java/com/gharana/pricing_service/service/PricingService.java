package com.gharana.pricing_service.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.gharana.pricing_service.dto.AvailableRoomTypeDTO;
import com.gharana.pricing_service.dto.AvgRoomTypePriceQuoteDTO;
import com.gharana.pricing_service.dto.MinHotelPriceQuoteDTO;

@Service
public interface PricingService {
    public List<MinHotelPriceQuoteDTO> getMinHotelRatePerNight(List<AvailableRoomTypeDTO> availableRoomTypes, LocalDate checkInDate, LocalDate checkOutDate);
    public List<AvgRoomTypePriceQuoteDTO> getAvgRoomTypePricePerNight(List<AvailableRoomTypeDTO> availableRoomTypes, LocalDate checkInDate, LocalDate checkOutDate);
}
