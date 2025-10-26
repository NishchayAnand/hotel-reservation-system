package com.gharana.inventory_service.model.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Entity
@Table(name = "hold_items")
public class HoldItem {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hold_id", nullable = false)
    private Hold hold;

    @Column(name = "hotel_id", nullable = false)
    private Long hotelId; // required in case we have duplicate roomTypeIds across hotels.

    @Column(name = "room_type_id", nullable = false)
    private Long roomTypeId;

    @Column(name = "check_in_date", nullable = false)
    private LocalDate checkInDate; // kept this outside of HoldItem to ensure normalization.

    @Column(name = "check_out_date", nullable = false)
    private LocalDate checkOutDate; // kept this outside of HoldItem to ensure normalization.

    @Column(name = "held_count", nullable = false)
    private Integer heldCount;

}
