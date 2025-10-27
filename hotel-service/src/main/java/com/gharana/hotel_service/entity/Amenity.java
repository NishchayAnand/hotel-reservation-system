package com.gharana.hotel_service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "amenities")
public class Amenity {

    @Id
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;
    
}
