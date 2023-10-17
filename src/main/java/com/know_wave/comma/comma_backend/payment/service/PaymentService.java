package com.know_wave.comma.comma_backend.payment.service;

import com.know_wave.comma.comma_backend.payment.dto.PaymentRequest;
import com.know_wave.comma.comma_backend.payment.dto.PaymentWebResult;
import com.know_wave.comma.comma_backend.payment.entity.PaymentType;

public interface PaymentService {

    PaymentWebResult ready(PaymentRequest t);

    void pay(PaymentRequest t);

    void refund(PaymentRequest t);

    void cancel(PaymentRequest t);

    boolean supports(PaymentType type);
}
