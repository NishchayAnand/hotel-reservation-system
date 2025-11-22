package com.gharana.inventory_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import com.gharana.inventory_service.model.entity.Hold;

import jakarta.persistence.LockModeType;

public interface HoldRepository extends JpaRepository<Hold, Long> {

    Optional<Hold> findByReservationId(Long reservationId);
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select h from Hold h where h.id = :id")
    Optional<Hold> findByIdForUpdate(Long id);
    
}
