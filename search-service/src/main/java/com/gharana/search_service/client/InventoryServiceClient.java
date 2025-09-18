package com.gharana.search_service.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.gharana.search_service.dto.AvailableRoomType;
import com.gharana.search_service.dto.InventoryQueryRequest;

@FeignClient(name = "inventory-service", url = "http://localhost:8082/inventory")
public interface InventoryServiceClient {

    @PostMapping("/query")
    List<AvailableRoomType> getAvailableRoomTypes(@RequestBody InventoryQueryRequest req);

}
