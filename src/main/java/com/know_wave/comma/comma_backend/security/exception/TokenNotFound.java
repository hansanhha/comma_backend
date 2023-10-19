package com.know_wave.comma.comma_backend.security.exception;

public class TokenNotFound extends RuntimeException{

    public TokenNotFound() {
    }

    public TokenNotFound(String message) {
        super(message);
    }
}
