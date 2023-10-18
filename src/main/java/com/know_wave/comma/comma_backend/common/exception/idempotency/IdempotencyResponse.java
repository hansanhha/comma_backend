package com.know_wave.comma.comma_backend.common.exception.idempotency;

public class IdempotencyResponse extends RuntimeException {

    public IdempotencyResponse(String message) {
        super(message);
    }

}
