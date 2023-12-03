package know_wave.comma.payment_.dto;

import know_wave.comma.payment_.dto.kakao.KakaoPayReadyResponse;

public record PaymentPrepareResult(String paymentRequestId, String transactionId, String redirectMobileWebUrl, String redirectPcWebUrl) {

    public static PaymentPrepareResult of(KakaoPayReadyResponse response, String paymentRequestId) {
        return new PaymentPrepareResult(paymentRequestId, response.tid(), response.next_redirect_mobile_url(), response.next_redirect_pc_url());
    }
}
