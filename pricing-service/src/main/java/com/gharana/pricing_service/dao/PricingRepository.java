package com.gharana.pricing_service.dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.gharana.pricing_service.dto.PricingRecord;

@Repository
public class PricingRepository {

    public List<PricingRecord> getPriceByRoomTypeAndDateRange(String hotelId, String roomTypeId, LocalDate checkInDate, LocalDate checkOutDate) {
        
        List<PricingRecord> priceList = List.of(
            new PricingRecord("101", "roomType1", LocalDate.of(2025, 9, 18), 4500),
            new PricingRecord("101", "roomType1", LocalDate.of(2025, 9, 19), 6000), 
            new PricingRecord("101", "roomType2", LocalDate.of(2025, 9, 18), 5000), 
            new PricingRecord("101", "roomType2", LocalDate.of(2025, 9, 19), 6000), 
            new PricingRecord("102", "roomType1", LocalDate.of(2025, 9, 18), 3500), 
            new PricingRecord("102", "roomType1", LocalDate.of(2025, 9, 19), 4000), 
            new PricingRecord("102", "roomType2", LocalDate.of(2025, 9, 18), 4000), 
            new PricingRecord("102", "roomType2", LocalDate.of(2025, 9, 19), 5000)
        );
        
        return priceList.stream()
            .filter( record -> record.getHotelId().equals(hotelId) )
            .filter( record -> record.getRoomTypeId().equals(roomTypeId) )
            .filter( record -> !record.getDate().isBefore(checkInDate) && record.getDate().isBefore(checkOutDate) )
            .toList();
 
    }

}
