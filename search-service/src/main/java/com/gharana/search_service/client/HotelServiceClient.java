package com.gharana.search_service.client;

import java.util.List;
import java.util.Set;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.gharana.search_service.dto.HotelDTO;
import com.gharana.search_service.dto.RoomTypeDTO;

@FeignClient(name = "hotel-service", url = "http://localhost:8081/api/hotels")
public interface HotelServiceClient {

    @GetMapping("")
    List<HotelDTO> getHotelsByLocationId(@RequestParam Long locationId);

    @GetMapping("/{hotelId}/room-types")
    List<RoomTypeDTO> getRoomTypesByIds(@PathVariable Long hotelId, @RequestBody Set<Long> roomTypeIds);


}
