package com.gharana.search_service.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.gharana.search_service.dto.AvgRoomTypePriceQuoteDTO;
import com.gharana.search_service.dto.MinHotelPriceQuoteDTO;
import com.gharana.search_service.dto.PricingQueryRequestDTO;

@FeignClient(name="pricing-service", url="${service.pricing-service.base-url}/api/pricing")
public interface PricingServiceClient {

    @PostMapping("/hotels")
    List<MinHotelPriceQuoteDTO> getMinHotelPricePerNight(@RequestBody PricingQueryRequestDTO req);

    @PostMapping("/room-types")
    List<AvgRoomTypePriceQuoteDTO> getAvgRoomTypePricePerNight(@RequestBody PricingQueryRequestDTO req);
    

}
