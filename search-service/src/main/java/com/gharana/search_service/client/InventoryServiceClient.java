package com.gharana.search_service.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.gharana.search_service.dto.RoomAvailabilityRequest;
import com.gharana.search_service.dto.RoomAvailabilityDTO;

@FeignClient(name = "inventory-service", url = "http://localhost:8082/api/inventory")
public interface InventoryServiceClient {

    @PostMapping("/get-availability")
    List<RoomAvailabilityDTO> getRoomAvailability(@RequestBody RoomAvailabilityRequest req);

}
