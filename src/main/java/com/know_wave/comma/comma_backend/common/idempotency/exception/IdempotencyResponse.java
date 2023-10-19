package com.know_wave.comma.comma_backend.common.idempotency.exception;

public class IdempotencyResponse extends RuntimeException {

    public IdempotencyResponse(String message) {
        super(message);
    }

}
