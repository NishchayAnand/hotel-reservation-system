package com.gharana.hotel_service.model.entity;

import java.util.Set;

import com.gharana.hotel_service.model.enums.BedType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "room_types")
@Data
public class RoomType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "hotel_id", nullable = false)
    private Long hotelId;

    @Column(nullable = false, length = 255)
    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "bed_type", columnDefinition = "room_bed_type")
    private BedType bedType;

    @Column(name = "bed_count")
    private Integer bedCount;

    @ManyToMany
    @JoinTable(
        name="room_amenities",
        joinColumns = @JoinColumn(name = "room_type_id"),
        inverseJoinColumns = @JoinColumn(name = "amenity_id"))
    private Set<Amenity> amenities;
    
}