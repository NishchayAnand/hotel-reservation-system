package com.gharana.search_service.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gharana.search_service.model.Hotel;
import com.gharana.search_service.service.SearchService;

@RestController
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/hotels")
    public List<Hotel> search(@RequestParam("destination") String destination, 
                                @RequestParam("check-in-date") LocalDate checkInDate, 
                                @RequestParam("check-out-date") LocalDate checkOutDate) {
        // Validate input parameters
        return searchService.search(destination, destination, destination);   
    }

}
