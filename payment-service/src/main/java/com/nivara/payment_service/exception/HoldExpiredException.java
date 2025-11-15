package com.nivara.payment_service.exception;

public class HoldExpiredException extends RuntimeException {
    
    public HoldExpiredException(String message) {
        super(message);
    }

}
