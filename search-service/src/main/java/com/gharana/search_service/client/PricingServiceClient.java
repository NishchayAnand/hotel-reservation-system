package com.gharana.search_service.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import com.gharana.search_service.dto.PricingQueryRequest;

@FeignClient(name="pricing-service", url="http://localhost:8083/pricing")
public interface PricingServiceClient {

    @PostMapping("/")
    List<Double> getPrice(PricingQueryRequest req);

}
