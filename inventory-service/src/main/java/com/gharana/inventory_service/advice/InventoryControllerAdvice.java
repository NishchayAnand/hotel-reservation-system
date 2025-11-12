package com.gharana.inventory_service.advice;

import java.time.Instant;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.gharana.inventory_service.exception.HoldReleasedException;
import com.gharana.inventory_service.exception.InventoryUnavailableException;
import com.gharana.inventory_service.model.dto.ErrorResponseDTO;

@RestControllerAdvice
public class InventoryControllerAdvice {

    // HTTP 422 - UNPROCESSABLE_ENTITY
    @ExceptionHandler(InventoryUnavailableException.class)
    public ResponseEntity<Object> handleUnavailableInventory(InventoryUnavailableException ex) {
        return build(HttpStatus.UNPROCESSABLE_ENTITY, "INVENTORY_UNAVAILABLE", ex.getMessage(), null);
    }

    // HTTP 410 - GONE
    @ExceptionHandler(HoldReleasedException.class)
    public ResponseEntity<Object> handleInvalidHold(HoldReleasedException ex) {
        return build(HttpStatus.GONE, "HOLD_RELEASED", ex.getMessage(), null);
    }

    // HTTP 409 - CONFLICT
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrity(DataIntegrityViolationException ex) {
        return build(HttpStatus.CONFLICT, "HOLD_ALREADY_EXISTS", ex.getMessage(), null);
    }

    private ResponseEntity<Object> build(HttpStatus status, String code, String message, Object details) {
        ErrorResponseDTO responseBody = new ErrorResponseDTO(code, message, details, Instant.now().toString());
        return ResponseEntity.status(status).body(responseBody);
    }

}
