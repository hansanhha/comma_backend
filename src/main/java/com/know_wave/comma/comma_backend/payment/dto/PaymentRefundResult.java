package com.know_wave.comma.comma_backend.payment.dto;

import java.time.LocalDateTime;

public record PaymentRefundResult(LocalDateTime refundedDate) {

    public static PaymentRefundResult of(LocalDateTime refundedDate) {
        return new PaymentRefundResult(refundedDate);
    }
}
