package com.MediLux.MediLux.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionsHandler {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleSomeException(NotFoundException ex) {
        // Handle the exception and return a ResponseEntity with an appropriate HTTP status code
        return new ResponseEntity<>("Nie znaleziono: " + ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}
