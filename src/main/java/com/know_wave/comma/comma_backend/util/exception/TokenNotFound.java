package com.know_wave.comma.comma_backend.util.exception;

public class TokenNotFound extends RuntimeException{

    public TokenNotFound() {
    }

    public TokenNotFound(String message) {
        super(message);
    }
}
