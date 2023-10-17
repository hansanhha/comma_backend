package com.know_wave.comma.comma_backend.payment.dto;

import com.know_wave.comma.comma_backend.payment.dto.kakao.KakaoPayReadyResponse;

public record PaymentAuthResult(String paymentRequestId, String transactionId, String redirectMobileWebUrl, String redirectPcWebUrl) {

    public static PaymentAuthResult of(KakaoPayReadyResponse response, String paymentRequestId) {
        return new PaymentAuthResult(paymentRequestId, response.tid(), response.next_redirect_mobile_url(), response.next_redirect_pc_url());
    }
}
