package com.know_wave.comma.comma_backend.payment.service;

import com.know_wave.comma.comma_backend.payment.dto.PaymentAuthRequest;
import com.know_wave.comma.comma_backend.payment.dto.PaymentAuthResult;
import com.know_wave.comma.comma_backend.payment.entity.Deposit;
import com.know_wave.comma.comma_backend.payment.entity.PaymentType;

public interface PaymentService {

    PaymentAuthResult ready(String idempotencyKey, PaymentAuthRequest request);

    void pay(Deposit deposit, String paymentToken);

    void refund(PaymentAuthRequest request);

    void cancel(PaymentAuthRequest request);

    boolean supports(PaymentType type);
}
