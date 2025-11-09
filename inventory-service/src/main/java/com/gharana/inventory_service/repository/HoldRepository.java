package com.gharana.inventory_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gharana.inventory_service.model.entity.Hold;

public interface HoldRepository extends JpaRepository<Hold, String> {
    public Optional<Hold> findByReservationId(String requestId);
}
