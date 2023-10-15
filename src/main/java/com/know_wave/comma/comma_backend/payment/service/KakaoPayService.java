package com.know_wave.comma.comma_backend.payment.service;

import com.know_wave.comma.comma_backend.payment.entity.*;
import org.springframework.stereotype.Service;

@Service
public class KakaoPayService implements PaymentService<KakaoPaymentReady, KakaoPaymentApprove> {

    @Override
    public KakaoPaymentApprove pay(KakaoPaymentReady request) {
        return null;
    }

    @Override
    public KakaoPaymentApprove refund(KakaoPaymentReady request) {
        return null;
    }

    @Override
    public KakaoPaymentApprove cancel(KakaoPaymentReady request) {
        return null;
    }

    @Override
    public boolean supports(PaymentType type) {
        return type == PaymentType.KAKAO_PAY;
    }
}
