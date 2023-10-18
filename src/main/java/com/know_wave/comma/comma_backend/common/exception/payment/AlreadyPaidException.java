package com.know_wave.comma.comma_backend.common.exception.payment;

public class AlreadyPaidException extends RuntimeException {

    public AlreadyPaidException(String message) {
        super(message);
    }
}
