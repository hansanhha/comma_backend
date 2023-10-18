package com.know_wave.comma.comma_backend.common.exception;

public class TokenNotFound extends RuntimeException{

    public TokenNotFound() {
    }

    public TokenNotFound(String message) {
        super(message);
    }
}
