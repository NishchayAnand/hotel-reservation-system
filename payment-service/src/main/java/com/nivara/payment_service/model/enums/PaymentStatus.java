package com.nivara.payment_service.model.enums;

public enum PaymentStatus {
    INIT,    // Payment row created (before PSP call)
    PENDING_ORDER_CREATION,    
    SUCCESS_ORDER_CREATION, // Razorpay order created successfully
    SUCCESS_CLIENT_CALLBACK, // signature valid, waiting orchestration
    SUCCESS,                // payment + downstream side effects done
    FAILED_CREATION,
    FAILED_ORDER_CREATION,
    FAILED_SIGNATURE,
    FAILED_INVENTORY,
    FAILED_RESERVATION,
    FAILED_UNKNOWN
}
