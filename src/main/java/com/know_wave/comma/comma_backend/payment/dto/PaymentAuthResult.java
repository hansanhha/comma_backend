package com.know_wave.comma.comma_backend.payment.dto;

import com.know_wave.comma.comma_backend.payment.dto.kakao.KakaoPayReadyResponse;

public record PaymentAuthResult(String transactionId, String redirectMobileWebUrl, String redirectPcWebUrl) {

    public static PaymentAuthResult of(KakaoPayReadyResponse response) {
        return new PaymentAuthResult(response.tid(), response.next_redirect_mobile_url(), response.next_redirect_pc_url());
    }
}
