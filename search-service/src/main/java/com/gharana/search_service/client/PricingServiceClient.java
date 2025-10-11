package com.gharana.search_service.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.gharana.search_service.dto.PriceQuoteDTO;
import com.gharana.search_service.dto.PricingQueryRequestDTO;

@FeignClient(name="pricing-service", url="http://localhost:8083/pricing")
public interface PricingServiceClient {

    @PostMapping("/query")
    List<PriceQuoteDTO> getAvgPricePerNight(@RequestBody PricingQueryRequestDTO req);

}
