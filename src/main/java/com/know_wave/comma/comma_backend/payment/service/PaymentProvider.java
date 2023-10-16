package com.know_wave.comma.comma_backend.payment.service;

import com.know_wave.comma.comma_backend.payment.dto.PaymentRequest;
import com.know_wave.comma.comma_backend.payment.entity.PaymentType;
import com.know_wave.comma.comma_backend.util.exception.NotFoundException;

public interface PaymentProvider<> {

    PaymentService<T, R> getPaymentService(PaymentType paymentType) throws NotFoundException;

    T getPaymentRequest(PaymentRequest request, PaymentType type);
}
