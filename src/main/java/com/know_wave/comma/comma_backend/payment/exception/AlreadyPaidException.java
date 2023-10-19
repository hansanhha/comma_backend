package com.know_wave.comma.comma_backend.payment.exception;

public class AlreadyPaidException extends RuntimeException {

    public AlreadyPaidException(String message) {
        super(message);
    }
}
