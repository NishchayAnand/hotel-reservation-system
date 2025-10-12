package com.gharana.search_service.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.gharana.search_service.client.HotelServiceClient;
import com.gharana.search_service.client.InventoryServiceClient;
import com.gharana.search_service.client.PricingServiceClient;
import com.gharana.search_service.dto.AvailableHotelDTO;
import com.gharana.search_service.dto.AvailableRoomTypeDTO;
import com.gharana.search_service.dto.HotelDTO;
import com.gharana.search_service.dto.InventoryQueryRequest;
import com.gharana.search_service.dto.MinPriceQuoteDTO;
import com.gharana.search_service.dto.PricingQueryRequestDTO;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SearchServiceImpl implements SearchService {

    private final HotelServiceClient hotelServiceClient;
    private final InventoryServiceClient inventoryServiceClient;
    private final PricingServiceClient pricingServiceClient;

    @Override
    public List<AvailableHotelDTO> search(Long locationId, LocalDate checkInDate, LocalDate checkOutDate) {

        List<AvailableHotelDTO> availableHotels = new ArrayList<>();
        
        // Step 1: Query Hotel Service for hotel metadata
        List<HotelDTO> hotels = hotelServiceClient.getHotelsByLocationId(locationId);
        if(hotels.isEmpty()) return List.of();

        // Step 2: Build a map of Hotel metadata for easy lookup
        Map<Long, HotelDTO> hotelById = hotels.stream().collect(Collectors.toMap(HotelDTO::getId, hotelObj -> hotelObj));

        // Step 3: Query Inventory Service to get list of room types which have rooms available on every date in the [chekInDate, checkOutDate) date range.
        List<AvailableRoomTypeDTO> availableRoomTypes = inventoryServiceClient
            .queryRoomAvailability(new InventoryQueryRequest(hotelById.keySet(), checkInDate, checkOutDate));
        
        // Step 4: for each availableRoomType, get avg rate per night
        List<MinPriceQuoteDTO> priceQuotes = pricingServiceClient.getMinPricePerNight(new PricingQueryRequestDTO(availableRoomTypes, checkInDate, checkOutDate));

        long nights = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        for(MinPriceQuoteDTO priceQuote: priceQuotes) {
            HotelDTO hotel = hotelById.get(priceQuote.getHotelId());
            availableHotels.add(
                AvailableHotelDTO.builder()
                    .id(hotel.getId())
                    .name(hotel.getName())
                    .address(hotel.getAddress())
                    .description(hotel.getDescription())
                    .thumbnailUrl(hotel.getThumbnailUrl())
                    .rating(hotel.getRating())
                    .amenities(hotel.getAmenities())
                    .nights(nights)
                    .avgRatePerNight(priceQuote.getMinRatePerNight())
                    .build()
            );
        }

        return availableHotels;
        
    }

}
