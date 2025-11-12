package com.nivara.reservation_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.nivara.reservation_service.config.feign.InventoryFeignConfig;
import com.nivara.reservation_service.model.dto.CreateHoldRequestDTO;
import com.nivara.reservation_service.model.dto.CreateHoldResponseDTO;

@FeignClient(
    name = "inventory-service", 
    url = "http://localhost:8082/api/inventory",
    configuration = InventoryFeignConfig.class
)
public interface InventoryClient {

    @PostMapping("create-hold")
    CreateHoldResponseDTO createHold(@RequestBody CreateHoldRequestDTO requestBody);

    @GetMapping("holds")
    CreateHoldResponseDTO getHoldByReservationId(@RequestParam Long reservationId);

}
