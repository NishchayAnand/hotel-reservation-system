package com.nivara.reservation_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nivara.reservation_service.model.entity.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long>  {
    Optional<Reservation> findByRequestId(String requestId);
    
}
