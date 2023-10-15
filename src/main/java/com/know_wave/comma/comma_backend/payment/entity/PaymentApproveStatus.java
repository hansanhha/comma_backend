package com.know_wave.comma.comma_backend.payment.entity;

public enum PaymentApproveStatus {

    READY("결제 승인 요청"),
    APPROVE("결제 승인"),
    CANCEL("결제 취소"),
    TIME_OUT("결제 승인 유효 시간 지남"),
    FAIL("결제 승인 실패");

    private final String value;

    PaymentApproveStatus(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
