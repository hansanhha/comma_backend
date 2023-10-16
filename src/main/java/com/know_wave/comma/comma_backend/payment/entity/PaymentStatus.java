package com.know_wave.comma.comma_backend.payment.entity;

public enum PaymentStatus {

    READY("결제 준비"),
    REQUEST("결제 요청"),
    APPROVE("결제 승인"),
    CANCEL("결제 취소"),
    TIME_OUT("결제 요청 유효 시간 지남"),
    AUTH_FAIL("결제 인증 실패"),
    INSUFFICIENT_BALANCE("잔액 부족");

    private final String value;

    PaymentStatus(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
