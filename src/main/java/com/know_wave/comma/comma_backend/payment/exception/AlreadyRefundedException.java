package com.know_wave.comma.comma_backend.payment.exception;

public class AlreadyRefundedException extends RuntimeException {
    public AlreadyRefundedException(String message) {
        super(message);
    }
}
