package com.nivara.payment_service.model.enums;

public enum PaymentStatus {
    CREATED,    // Payment row created (before PSP call)
    PENDING,    // Razorpay order created successfully
    COMPLETED,  // Payment sucess webhook
    FAILED      // Payment failure webhook
}
