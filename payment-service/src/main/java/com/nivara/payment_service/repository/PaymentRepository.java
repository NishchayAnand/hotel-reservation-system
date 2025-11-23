package com.nivara.payment_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nivara.payment_service.model.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    public Optional<Payment> findByReservationId(Long reservationId);
    public Optional<Payment> findByRazorpayOrderId(String razorpayOrderId);
}
