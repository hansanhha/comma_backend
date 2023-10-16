package com.know_wave.comma.comma_backend.payment.entity;

public enum DepositStatus {

    EMPTY("보증금 제출 안함"),
    SUBMIT("보증금 제출"),
    REFUND("보증금 반환"),
    DONATE("보증금 기부");

    private final String value;

    DepositStatus(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
