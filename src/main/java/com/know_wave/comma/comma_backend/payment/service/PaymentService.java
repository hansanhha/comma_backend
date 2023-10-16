package com.know_wave.comma.comma_backend.payment.service;

import com.know_wave.comma.comma_backend.payment.entity.PaymentType;

import java.util.Map;

public interface PaymentService<T, R> {

    R ready(T t);

    R pay(T t);

    R refund(T t);

    R cancel(T t);

    boolean supports(PaymentType type);
}
