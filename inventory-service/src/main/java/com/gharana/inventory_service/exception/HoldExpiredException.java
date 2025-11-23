package com.gharana.inventory_service.exception;

import java.time.Instant;

public class HoldExpiredException extends RuntimeException {

    public HoldExpiredException(Long holdId, Instant expires_at) {
        super("Hold: " + holdId + " expired at: " + expires_at);
    }

}
