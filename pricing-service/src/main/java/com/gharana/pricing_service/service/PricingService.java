package com.gharana.pricing_service.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.gharana.pricing_service.dto.AvailableRoomTypeDTO;
import com.gharana.pricing_service.dto.MinPriceQuoteDTO;

@Service
public interface PricingService {
    public List<MinPriceQuoteDTO> getMinRatePerNight(List<AvailableRoomTypeDTO> availableRoomTypes, LocalDate checkInDate, LocalDate checkOutDate);
}
