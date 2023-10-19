package com.know_wave.comma.comma_backend.common.idempotency.exception;

public class IdempotencyUnprocessableEntity extends RuntimeException {
    public IdempotencyUnprocessableEntity(String message) {
        super(message);
    }
}
