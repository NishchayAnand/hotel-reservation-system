package com.gharana.inventory_service.exception;

public class HoldNotFoundException extends RuntimeException {

    public HoldNotFoundException(Long holdId) {
        super("Hold not found: " + holdId);
    }

}
