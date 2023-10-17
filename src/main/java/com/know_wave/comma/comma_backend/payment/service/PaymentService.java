package com.know_wave.comma.comma_backend.payment.service;

import com.know_wave.comma.comma_backend.payment.dto.PaymentAuthRequest;
import com.know_wave.comma.comma_backend.payment.dto.PaymentAuthResult;
import com.know_wave.comma.comma_backend.payment.entity.Deposit;
import com.know_wave.comma.comma_backend.payment.entity.PaymentType;

public interface PaymentService {

    PaymentAuthResult ready(PaymentAuthRequest t);

    void pay(Deposit deposit, String paymentToken);

    void refund(PaymentAuthRequest t);

    void cancel(PaymentAuthRequest t);

    boolean supports(PaymentType type);
}
