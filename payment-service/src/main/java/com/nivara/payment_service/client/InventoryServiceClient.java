package com.nivara.payment_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.nivara.payment_service.model.dto.ConsumeHoldResponseDTO;
import com.nivara.payment_service.model.dto.HoldDTO;

@FeignClient(name = "inventory-service", url = "http://localhost:8082/api/inventory")
public interface InventoryServiceClient {

    @GetMapping("/holds/{holdId}")
    HoldDTO getHold(@RequestParam Long holdId);

    @PostMapping("holds/{holdId}/consume")
    ConsumeHoldResponseDTO consumeHold(
        @PathVariable Long holdId, 
        @RequestParam Long reservationId, 
        @RequestParam Long paymentId
    );

}
