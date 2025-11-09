package com.nivara.reservation_service.exception;

/*
 * Generic exception for transient server or network issues (5xx / connection problems).
 * These exceptions are typically considered retryable by the resilience layer.
*/
public class RemoteServerException extends RuntimeException {

    public RemoteServerException(String message) {
        super(message);
    }

}
