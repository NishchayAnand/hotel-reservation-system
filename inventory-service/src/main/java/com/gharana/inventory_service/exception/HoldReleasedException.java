package com.gharana.inventory_service.exception;

public class HoldReleasedException extends RuntimeException {

    public HoldReleasedException(String message) {
        super(message);
    }

}
