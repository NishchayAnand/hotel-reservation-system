package com.nivara.reservation_service.model.enums;

public enum ReservationStatus {
    PENDING,
    HOLD_CREATED,
    ORDER_CREATED,
    AWAITING_PAYMENT,
    CONFIRMED, // if payment is success
    CANCELLED, // if user cancels or payment fails
    EXPIRED    // if user doesn't pay before the hold expires
}
