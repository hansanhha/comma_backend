package com.know_wave.comma.comma_backend.payment.entity;

public enum PaymentMethodType {

    CARD("카드"),
    MONEY("현금");

    private final String type;

    PaymentMethodType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }
}
