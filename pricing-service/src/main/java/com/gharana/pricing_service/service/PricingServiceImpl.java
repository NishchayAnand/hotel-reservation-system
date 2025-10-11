package com.gharana.pricing_service.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.gharana.pricing_service.Repository.PricingRepository;
import com.gharana.pricing_service.dto.AvailableRoomTypeDTO;
import com.gharana.pricing_service.dto.PriceQuoteDTO;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PricingServiceImpl implements PricingService {

    private final PricingRepository pricingRepository;

    @Override
    public List<PriceQuoteDTO> getAvgRatePerNight(List<AvailableRoomTypeDTO> availableRoomTypes, LocalDate checkInDate, LocalDate checkOutDate) {
        List<Object[]> rows = pricingRepository.getAvgRateByHotelAndRoomType(availableRoomTypes, checkInDate, checkOutDate);
        return rows.stream()
            .map(row -> new PriceQuoteDTO(
                (Long) row[0], 
                (Long) row[1],
                (BigDecimal) row[2]))
            .collect(Collectors.toList());
    }

}
