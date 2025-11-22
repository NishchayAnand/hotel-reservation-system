package com.nivara.payment_service.model.enums;

public enum PaymentStatus {
    INITIATED,      // We created the payment record, user not paid yet
    PENDING,        // User is on gateway / waiting for PSP callback
    AUTHORIZED,     // Payment signature validated, ready for fulfillment
    COMPLETED,      // Money sucessfully captured and our business flow completed
    FAILED         // Payment could not be completed (no money captured)
    //REVERSED        // We had money, but later refunded or reversed it
}
