package com.know_wave.comma.comma_backend.payment.dto;

import com.know_wave.comma.comma_backend.payment.dto.kakao.KakaoPayReadyResponse;

public record PaymentPrepareResult(String paymentRequestId, String transactionId, String redirectMobileWebUrl, String redirectPcWebUrl) {

    public static PaymentPrepareResult of(KakaoPayReadyResponse response, String paymentRequestId) {
        return new PaymentPrepareResult(paymentRequestId, response.tid(), response.next_redirect_mobile_url(), response.next_redirect_pc_url());
    }
}
