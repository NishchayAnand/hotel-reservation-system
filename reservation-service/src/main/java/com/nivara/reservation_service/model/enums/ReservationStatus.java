package com.nivara.reservation_service.model.enums;

public enum ReservationStatus {
    PENDING,
    AWAITING_PAYMENT, // if hold is success
    FAILED,           // if payment fails
    CONFIRMED,        // if payment is success
    CANCELLED,        // if user cancels
    HOLD_EXPIRED      // if user doesn't pay before the hold expires
}
