package com.nivara.payment_service.exception;

public class HoldExpiringSoonException extends RuntimeException {

    public HoldExpiringSoonException(String message) {
        super(message);
    }

}
