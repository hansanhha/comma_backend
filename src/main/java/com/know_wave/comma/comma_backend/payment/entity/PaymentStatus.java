package com.know_wave.comma.comma_backend.payment.entity;

public enum PaymentStatus {

    READY("결제 준비"),
    REQUEST("결제 요청"),
    REQUEST_APPROVE("결제 승인 요청"),
    APPROVE("결제 승인"),
    CANCEL("결제 취소"),
    FAIL("결제 요청 유효 시간 지남");

    private final String value;

    PaymentStatus(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
