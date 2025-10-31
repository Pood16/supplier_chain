package org.tricol.supplierchain.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.tricol.supplierchain.exception.DuplicateResourceException;

import java.util.HashMap;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<HashMap<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
        HashMap<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach((error) -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<HashMap<String, String>> handleNotFound(NoSuchElementException ex) {
        HashMap<String, String> errors = new HashMap<>();
        errors.put("error", ex.getMessage());
        errors.put("status", "404");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errors);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<HashMap<String, String>> handleDuplicate(DuplicateResourceException ex) {
        HashMap<String, String> errors = new HashMap<>();
        errors.put("error", ex.getMessage());
        errors.put("status", "409");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<HashMap<String, String>> handleGlobal(Exception ex) {
        HashMap<String, String> errors = new HashMap<>();
        errors.put("error", ex.getMessage());
        errors.put("status", "500");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errors);
    }
}
