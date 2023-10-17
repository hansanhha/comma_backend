package com.know_wave.comma.comma_backend.payment.dto;

import com.know_wave.comma.comma_backend.payment.dto.kakao.KaKaoPaymentReadyResponse;

public record PaymentWebResult(String transactionId, String redirectMobileWebUrl, String redirectPcWebUrl) {
    public static PaymentWebResult ofReady(KaKaoPaymentReadyResponse response) {
        return new PaymentWebResult(response.tid(), response.next_redirect_mobile_url(), response.next_redirect_pc_url());
    }

}
