package com.nivara.reservation_service.exception;

/*
 * Thrown when the requested inventory is no longer available (HTTP 409).
 * This is a business level exception and should NOT be retried.
 */
public class InventoryUnavailableException extends RuntimeException {

    public InventoryUnavailableException(String message) {
        super(message);
    }

}
