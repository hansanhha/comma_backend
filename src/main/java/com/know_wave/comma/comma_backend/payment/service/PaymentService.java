package com.know_wave.comma.comma_backend.payment.service;

import com.know_wave.comma.comma_backend.payment.dto.PaymentAuthRequest;
import com.know_wave.comma.comma_backend.payment.dto.PaymentAuthResult;
import com.know_wave.comma.comma_backend.payment.dto.PaymentRefundRequest;
import com.know_wave.comma.comma_backend.payment.dto.PaymentRefundResult;
import com.know_wave.comma.comma_backend.payment.entity.Deposit;
import com.know_wave.comma.comma_backend.payment.entity.PaymentType;

public interface PaymentService {

    PaymentAuthResult ready(String idempotencyKey, PaymentAuthRequest request);

    void pay(Deposit deposit, String paymentToken);

    PaymentRefundResult refund(Deposit request);

    void cancel(Deposit request);

    boolean supports(PaymentType type);
}
