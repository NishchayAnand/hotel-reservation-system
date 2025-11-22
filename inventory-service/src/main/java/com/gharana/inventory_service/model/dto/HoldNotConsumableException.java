package com.gharana.inventory_service.model.dto;

public class HoldNotConsumableException extends RuntimeException {

    public HoldNotConsumableException(String message) {
        super(message);
    }

}
