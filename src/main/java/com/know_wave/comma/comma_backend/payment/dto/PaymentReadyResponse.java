package com.know_wave.comma.comma_backend.payment.dto;

import com.know_wave.comma.comma_backend.payment.entity.KakaoPaymentReady;
import com.know_wave.comma.comma_backend.payment.entity.Payment;

public class PaymentReadyResponse{

    public static PaymentReadyResponse of(Payment ready) {
        if (ready instanceof KakaoPaymentReady kakaoPaymentReady) {
            return new PaymentReadyResponse(
                    kakaoPaymentReady.getNextRedirectMobileUrl(),
                    kakaoPaymentReady.getNextRedirectPcUrl());
        }
        return null;
    }

    private PaymentReadyResponse(String redirectMobileWebUrl, String redirectPcWebUrl) {
        this.redirectMobileWebUrl = redirectMobileWebUrl;
        this.redirectPcWebUrl = redirectPcWebUrl;
    }/**/

    private String redirectMobileWebUrl;
    private String redirectPcWebUrl;
}
