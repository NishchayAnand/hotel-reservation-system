package com.gharana.pricing_service.dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.gharana.pricing_service.model.PricingRecord;

@Repository
public class PricingRepository {

    public List<PricingRecord> getPrice(String hotelId, String roomTypeId, LocalDate checkInDate, LocalDate checkOutDate) {
       return List.of(
        new PricingRecord("Hotel1", "RoomType1", LocalDate.now(), 4500.0),
        new PricingRecord("Hotel1", "RoomType1", LocalDate.now().plusDays(1), 5000.0)
       );
    }

}
