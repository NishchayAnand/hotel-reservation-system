package com.nivara.reservation_service.exception;

public class HoldExpiredException extends RuntimeException {

    public HoldExpiredException(String message) {
        super(message);
    }

}
