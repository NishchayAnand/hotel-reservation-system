package com.gharana.inventory_service.advice;

import java.time.Instant;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.gharana.inventory_service.exception.HoldUnavailableException;
import com.gharana.inventory_service.exception.InventoryUnavailableException;
import com.gharana.inventory_service.model.dto.ErrorResponseDTO;

@RestControllerAdvice
public class InventoryControllerAdvice {

    @ExceptionHandler(InventoryUnavailableException.class)
    public ResponseEntity<Object> handleInventoryUnavailable(InventoryUnavailableException ex) {
        return build(HttpStatus.CONFLICT, "INVENTORY_UNAVAILABLE", ex.getMessage(), null);
    }

    @ExceptionHandler(HoldUnavailableException.class)
    public ResponseEntity<Object> handleHoldUnavailable(HoldUnavailableException ex) {
        return build(HttpStatus.CONFLICT, "HOLD_UNAVAILABLE", ex.getMessage(), null);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrity(DataIntegrityViolationException ex) {
        // Map duplicate-key to 409, other constraint violations to 500
        String msg = ex.getMostSpecificCause() != null ? ex.getMostSpecificCause().getMessage() : ex.getMessage();
        if (msg != null && msg.toLowerCase().contains("unique")) {
            return build(HttpStatus.CONFLICT, "DATA_INTEGRITY_VIOLATION", msg, null);
        }
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "DATA_INTEGRITY_ERROR", msg, null);
    }

    private ResponseEntity<Object> build(HttpStatus status, String code, String message, Object details) {
        ErrorResponseDTO responseBody = new ErrorResponseDTO(code, message, details, Instant.now().toString());
        return ResponseEntity.status(status).body(responseBody);
    }

}
