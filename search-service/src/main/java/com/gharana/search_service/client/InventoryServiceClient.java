package com.gharana.search_service.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import com.gharana.search_service.InventoryQueryRequest;
import com.gharana.search_service.dto.AvailableRoomType;

@FeignClient(name = "inventory-service", url = "http://localhost:8082/inventory")
public interface InventoryServiceClient {

    @PostMapping("/")
    List<AvailableRoomType> getAvailableRoomTypes(InventoryQueryRequest req);

}
