package com.know_wave.comma.comma_backend.web.exception;

import jakarta.persistence.EntityNotFoundException;

public class EmailNotFoundException extends EntityNotFoundException {
    public EmailNotFoundException() {
        super();
    }

    public EmailNotFoundException(Exception cause) {
        super(cause);
    }

    public EmailNotFoundException(String message) {
        super(message);
    }

    public EmailNotFoundException(String message, Exception cause) {
        super(message, cause);
    }
}
