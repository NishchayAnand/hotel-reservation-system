package com.gharana.search_service.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gharana.search_service.dto.AvailableHotelDTO;
import com.gharana.search_service.service.SearchService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/search")
@CrossOrigin(origins = "http://localhost:3000")
@AllArgsConstructor
public class MainController {

    private final SearchService searchService;

    @GetMapping("")
    public List<AvailableHotelDTO> search(@RequestParam Long locationId, 
                                @RequestParam LocalDate checkInDate, 
                                @RequestParam LocalDate checkOutDate) {
        // Validate input parameters
        return searchService.search(locationId, checkInDate, checkOutDate);   
    }

}
