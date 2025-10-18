package com.gharana.search_service.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.gharana.search_service.client.HotelServiceClient;
import com.gharana.search_service.client.InventoryServiceClient;
import com.gharana.search_service.client.PricingServiceClient;
import com.gharana.search_service.dto.AvailableHotelDTO;
import com.gharana.search_service.dto.AvailableRoomTypeDTO;
import com.gharana.search_service.dto.AvgRoomTypePriceQuoteDTO;
import com.gharana.search_service.dto.HotelDTO;
import com.gharana.search_service.dto.RoomAvailabilityRequest;
import com.gharana.search_service.dto.MinHotelPriceQuoteDTO;
import com.gharana.search_service.dto.PricingQueryRequestDTO;
import com.gharana.search_service.dto.RoomAvailabilityDTO;
import com.gharana.search_service.dto.RoomTypeDTO;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SearchServiceImpl implements SearchService {

    private final HotelServiceClient hotelServiceClient;
    private final InventoryServiceClient inventoryServiceClient;
    private final PricingServiceClient pricingServiceClient;

    @Override
    public List<AvailableHotelDTO> getAvailableHotelsByLocationId(Long locationId, LocalDate checkInDate, LocalDate checkOutDate) {
        
        // Step 1: Query Hotel Service for hotel metadata
        List<HotelDTO> hotels = hotelServiceClient.getHotelsByLocationId(locationId);
        if(hotels.isEmpty()) return List.of();

        // Step 2: Build a map of Hotel metadata for easy lookup
        Map<Long, HotelDTO> hotelById = hotels.stream().collect(Collectors.toMap(HotelDTO::getId, hotelObj -> hotelObj));

        // Step 3: Query Inventory Service to get list of room types which have rooms available on every date in the [chekInDate, checkOutDate) date range.
        // only need [hotelId, roomTypeId] --> application of graphQL API
        List<RoomAvailabilityDTO> availableRoomInventory = inventoryServiceClient
            .getRoomAvailability(new RoomAvailabilityRequest(hotelById.keySet(), checkInDate, checkOutDate));
        
        // Step 4: for each availableRoomType, get avg rate per night
        List<MinHotelPriceQuoteDTO> minPriceQuotes = pricingServiceClient.getMinHotelPricePerNight(new PricingQueryRequestDTO(availableRoomInventory, checkInDate, checkOutDate));

        return minPriceQuotes.stream()
            .map(priceQuote -> {
            HotelDTO hotel = hotelById.get(priceQuote.getHotelId());
            return AvailableHotelDTO.builder()
                .id(hotel.getId())
                .name(hotel.getName())
                .address(hotel.getAddress())
                .shortDescription(hotel.getShortDescription())
                .thumbnailUrl(hotel.getThumbnailUrl())
                .rating(hotel.getRating())
                .amenities(hotel.getAmenities())
                .minAvgRatePerNight(priceQuote.getMinAvgRatePerNight())
                .build();
            })
            .collect(Collectors.toList());
        
    }

    @Override
    public List<AvailableRoomTypeDTO> getAvailableRoomTypesByHotelId(Long hotelId, LocalDate checkInDate,
            LocalDate checkOutDate) {

        // Step 1: Query inventory service to fetch inventory details for all available room types associated with the specified hotelId.
        // only need [roomTypeId, availableCount (totalCount - reservedCount)] --> application of GraphQL API
        List<RoomAvailabilityDTO> availableRoomInventory = inventoryServiceClient
            .getRoomAvailability(new RoomAvailabilityRequest(Set.of(hotelId), checkInDate, checkOutDate));

        // Step 2: Build a map of room type IDs to their availability count for easy lookup.
        Map<Long, Integer> availabilityByRoomType = availableRoomInventory.stream()
                .collect(Collectors.toMap(RoomAvailabilityDTO::getRoomTypeId, RoomAvailabilityDTO::getAvailableRoomCount));

        // Step 3: Query pricing service to fetch average price per night for the room types that have availability across the [checkInDate, checkOutDate) range.
        List<AvgRoomTypePriceQuoteDTO> availableRoomRates = pricingServiceClient.getAvgRoomTypePricePerNight(new PricingQueryRequestDTO(availableRoomInventory, checkInDate, checkOutDate));

        // Step 4: Build a map of room type IDs to their availability count for easy lookup.
        Map<Long, BigDecimal> pricingByRoomType = availableRoomRates.stream()
                .collect(Collectors.toMap(AvgRoomTypePriceQuoteDTO::getRoomTypeId, AvgRoomTypePriceQuoteDTO::getAvgPricePerNight));

        // Step 5: Query hotel service to fetch room type details for each available room type.
        List<RoomTypeDTO> roomTypes = hotelServiceClient.getRoomTypesByIds(hotelId, availabilityByRoomType.keySet());

        return roomTypes.stream()
                .map(roomType -> {
                    Integer availableRoomCount = availabilityByRoomType.get(roomType.getId());
                    BigDecimal avgPricePerNight = pricingByRoomType.get(roomType.getId());
                    return AvailableRoomTypeDTO.builder()
                        .id(roomType.getId())
                        .name(roomType.getName())
                        .description(roomType.getDescription())
                        .bedType(roomType.getBedType())
                        .bedCount(roomType.getBedCount())
                        .thumbnailUrl(null)
                        .availableRoomCount(availableRoomCount)
                        .avgPricePerNight(avgPricePerNight)
                        .amenities(roomType.getAmenities())
                        .build();       
                })
                .collect(Collectors.toList());

    }

}
