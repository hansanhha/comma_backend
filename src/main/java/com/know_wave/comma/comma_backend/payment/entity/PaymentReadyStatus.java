package com.know_wave.comma.comma_backend.payment.entity;

public enum PaymentReadyStatus {

    READY("결제 준비"),
    REQUEST("결제 요청"),
    CANCEL("결제 취소"),
    FAIL("결제 요청 유효 시간 지남");

    private final String value;

    PaymentReadyStatus(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
