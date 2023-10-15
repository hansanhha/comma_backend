package com.know_wave.comma.comma_backend.payment.entity;

public enum PaymentApproveStatus {

    READY("결제 승인 요청"),
    APPROVE("결제 승인"),
    CANCEL("결제 취소"),
    FAIL("결제 승인 요청 유효 시간 지남");

    private final String value;

    PaymentApproveStatus(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
