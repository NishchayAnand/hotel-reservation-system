package com.nivara.payment_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.nivara.payment_service.model.dto.HoldDTO;

@FeignClient(name = "inventory-service", url="http://localhost:8082/api/inventory")
public interface InventoryServiceClient {

    @PostMapping("/holds/{holdId}")
    HoldDTO getHold(@RequestParam Long holdId);

}
