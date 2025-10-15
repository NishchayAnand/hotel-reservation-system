package com.gharana.pricing_service.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.gharana.pricing_service.Repository.PricingRepository;
import com.gharana.pricing_service.dto.AvailableRoomTypeDTO;
import com.gharana.pricing_service.dto.AvgRoomTypePriceQuoteDTO;
import com.gharana.pricing_service.dto.MinHotelPriceQuoteDTO;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PricingServiceImpl implements PricingService {

    private final PricingRepository pricingRepository;

    @Override
    public List<MinHotelPriceQuoteDTO> getMinHotelRatePerNight(List<AvailableRoomTypeDTO> availableRoomTypes, LocalDate checkInDate, LocalDate checkOutDate) {
        
        List<Object[]> rows = pricingRepository.getAvgRateByHotelAndRoomType(availableRoomTypes, checkInDate, checkOutDate); // row: [hotel_id, room_type_id, avg_price_per_night]
        
        Map<Long, BigDecimal> minByHotel = rows.stream()
            .collect(Collectors.toMap(
            row -> (Long) row[0], // hotelId
            row -> (BigDecimal) row[2], // avgRate
            (a, b) -> a.compareTo(b) <= 0 ? a : b // merge function: keep the smaller value
        ));

        return minByHotel.entrySet().stream()
            .map(e -> new MinHotelPriceQuoteDTO(e.getKey(), e.getValue()))
            .collect(Collectors.toList());
        
    }

    @Override
    public List<AvgRoomTypePriceQuoteDTO> getAvgRoomTypePricePerNight(List<AvailableRoomTypeDTO> availableRoomTypes,
            LocalDate checkInDate, LocalDate checkOutDate) {
        
        List<Object[]> rows = pricingRepository.getAvgRateByHotelAndRoomType(availableRoomTypes, checkInDate, checkOutDate); // row: [hotel_id, room_type_id, avg_price_per_night]

        List<AvgRoomTypePriceQuoteDTO> avgPriceQuotes = rows.stream()
            .map(row -> new AvgRoomTypePriceQuoteDTO(
            (Long) row[0], // hotelId
            (Long) row[1], // roomTypeId
            (BigDecimal) row[2] // avgPricePerNight
            ))
            .collect(Collectors.toList());

        return avgPriceQuotes;

    }

}
