package com.gharana.pricing_service.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gharana.pricing_service.dao.PricingRepository;
import com.gharana.pricing_service.model.PricingRecord;

@Service
public class PricingServiceImpl implements PricingService {

    @Autowired
    private PricingRepository pricingRepository;

    public PricingServiceImpl(PricingRepository pricingRepository) {
        this.pricingRepository = pricingRepository;
    }

    @Override
    public List<Double> getPrice(String hotelId, String roomTypeId, LocalDate checkInDate, LocalDate checkOutDate) {
        List<PricingRecord> records = pricingRepository.getPrice(hotelId, roomTypeId, checkInDate, checkOutDate);
        return records.stream().map(PricingRecord::getRate).toList();
    }

}
