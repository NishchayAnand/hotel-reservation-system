package com.nivara.payment_service.exception;

public class PaymentInvalidException extends RuntimeException {

    public PaymentInvalidException(String message) {
        super(message);
    }

}
