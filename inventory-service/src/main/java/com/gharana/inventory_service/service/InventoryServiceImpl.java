package com.gharana.inventory_service.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gharana.inventory_service.model.InventoryRecord;

@Service
public class InventoryServiceImpl implements InventoryService {

    @Override
    public List<InventoryRecord> queryAvailability(List<String> hotelIds, String checkInDate, String checkOutDate) {
        
        return null;
    }

}
