package com.know_wave.comma.comma_backend.payment.dto;

import com.know_wave.comma.comma_backend.payment.entity.PaymentType;

public record PaymentPrepareDto(
        String tempOrderId,
        PaymentType paymentType) {

        public static PaymentPrepareDto of(PaymentRequest request) {
                return new PaymentPrepareDto(
                        request.getArduinoOrderId(),
                        request.getPaymentType()
                );
        }
}
