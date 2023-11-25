package com.MediLux.MediLux.Exceptions;

import lombok.NoArgsConstructor;


public class NotFoundException extends RuntimeException {
    public NotFoundException() {

    }
    public NotFoundException(String message) {
        super(message);
    }
}
