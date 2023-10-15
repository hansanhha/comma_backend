package com.know_wave.comma.comma_backend.payment.service;

import com.know_wave.comma.comma_backend.payment.entity.PaymentReady;
import com.know_wave.comma.comma_backend.payment.entity.PaymentApprove;
import com.know_wave.comma.comma_backend.payment.entity.PaymentType;

public interface PaymentService<T extends PaymentReady, R extends PaymentApprove> {

    R pay(T request);

    R refund(T request);

    R cancel(T request);

    boolean supports(PaymentType type);
}
