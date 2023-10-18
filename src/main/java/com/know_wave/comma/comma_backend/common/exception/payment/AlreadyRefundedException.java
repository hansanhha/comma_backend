package com.know_wave.comma.comma_backend.common.exception.payment;

public class AlreadyRefundedException extends RuntimeException {
    public AlreadyRefundedException(String message) {
        super(message);
    }
}
