package com.know_wave.comma.comma_backend.payment.service;

import com.know_wave.comma.comma_backend.payment.entity.PaymentReady;
import com.know_wave.comma.comma_backend.payment.entity.PaymentApprove;
import com.know_wave.comma.comma_backend.payment.entity.PaymentType;
import com.know_wave.comma.comma_backend.util.exception.NotFoundException;

public interface PaymentProvider<T extends PaymentReady, R extends PaymentApprove> {

    PaymentService<T, R> getPaymentService(PaymentType paymentType) throws NotFoundException;

}
