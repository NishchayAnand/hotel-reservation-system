package com.nivara.payment_service.exception;

public class HoldConfirmedException extends RuntimeException {

    public HoldConfirmedException(String message) {
        super(message);
    }

}
