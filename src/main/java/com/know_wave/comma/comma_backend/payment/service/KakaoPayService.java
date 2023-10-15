package com.know_wave.comma.comma_backend.payment.service;

import com.know_wave.comma.comma_backend.payment.entity.KakaoPaymentApprove;
import com.know_wave.comma.comma_backend.payment.entity.KakaoPaymentReady;
import com.know_wave.comma.comma_backend.payment.entity.PaymentType;
import org.springframework.stereotype.Service;

@Service
public class KakaoPayService implements PaymentService<KakaoPaymentReady, KakaoPaymentApprove> {

    @Override
    public KakaoPaymentReady ready(KakaoPaymentReady request) {

    }

    @Override
    public KakaoPaymentApprove pay(KakaoPaymentReady request) {
        ready(request);
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
