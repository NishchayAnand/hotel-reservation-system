package com.gharana.inventory_service.model.entity;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import com.gharana.inventory_service.model.enums.HoldStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "holds")
public class Hold {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reservation_id", nullable = false, unique = true)
    private Long reservationId;

    @Column(name = "payment_id", unique = true)
    private Long paymentId; // to ensure idempotency check when the payment-service calls consumeHold()

    @Column(name = "hotel_id", nullable = false)
    private Long hotelId;

    @Column(name = "check_in_date", nullable = false)
    private LocalDate checkInDate;

    @Column(name = "check_out_date", nullable = false)
    private LocalDate checkOutDate;

    @OneToMany(mappedBy = "hold", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<HoldItem> heldItems;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private HoldStatus status;

    @Column(name = "expires_at")
    private Instant expiresAt;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    public boolean isExpired() {
        return status == HoldStatus.ACTIVE && Instant.now().isAfter(expiresAt);
    }

    public boolean isConsumed() {
        return status == HoldStatus.CONSUMED;
    }

}
