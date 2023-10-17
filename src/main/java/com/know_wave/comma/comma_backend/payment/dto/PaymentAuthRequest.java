package com.know_wave.comma.comma_backend.payment.dto;

import com.know_wave.comma.comma_backend.payment.entity.PaymentType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record PaymentAuthRequest(
        String accountId,
        String arduinoOrderId,
        PaymentType paymentType) {

        public static PaymentAuthRequest of(PaymentRequest request) {
                return new PaymentAuthRequest(
                        request.getAccountId(),
                        request.getArduinoOrderId(),
                        request.getPaymentType()
                );
        }
}
