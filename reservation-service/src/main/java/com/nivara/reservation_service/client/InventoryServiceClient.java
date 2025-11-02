package com.nivara.reservation_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import com.nivara.reservation_service.model.dto.CreateHoldRequest;
import com.nivara.reservation_service.model.dto.CreateHoldResponse;

@FeignClient(name = "inventory-service", url = "http://localhost:8082/api/inventory")
public interface InventoryServiceClient {

    @PostMapping("create-hold")
    public CreateHoldResponse createHold(String requestId, CreateHoldRequest requestBody);

}
