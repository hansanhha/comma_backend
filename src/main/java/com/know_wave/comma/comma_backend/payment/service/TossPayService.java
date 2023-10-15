package com.know_wave.comma.comma_backend.payment.service;

import com.know_wave.comma.comma_backend.payment.entity.PaymentType;
import com.know_wave.comma.comma_backend.payment.entity.TossPaymentReady;
import com.know_wave.comma.comma_backend.payment.entity.TossPaymentApprove;
import org.springframework.stereotype.Service;

@Service
public class TossPayService implements PaymentService<TossPaymentReady, TossPaymentApprove> {

    @Override
    public TossPaymentReady ready(TossPaymentReady request) {
        return null;
    }

    @Override
    public TossPaymentApprove pay(TossPaymentReady request) {
        return null;
    }

    @Override
    public TossPaymentApprove refund(TossPaymentReady request) {
        return null;
    }

    @Override
    public TossPaymentApprove cancel(TossPaymentReady request) {
        return null;
    }

    @Override
    public boolean supports(PaymentType type) {
        return type == PaymentType.TOSS_PAY;
    }
}
