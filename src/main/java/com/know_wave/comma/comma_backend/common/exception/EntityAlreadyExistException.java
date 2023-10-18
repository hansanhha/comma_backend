package com.know_wave.comma.comma_backend.common.exception;

import jakarta.persistence.EntityExistsException;

public class EntityAlreadyExistException extends EntityExistsException {
    public EntityAlreadyExistException() {
    }

    public EntityAlreadyExistException(String message) {
        super(message);
    }
}
