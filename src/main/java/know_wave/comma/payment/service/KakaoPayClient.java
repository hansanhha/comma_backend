package know_wave.comma.payment.service;

import know_wave.comma.payment.dto.client.PaymentClientApproveResponse;
import know_wave.comma.payment.dto.client.PaymentClientReadyResponse;
import know_wave.comma.payment.dto.client.PaymentClientRefundResponse;
import know_wave.comma.payment.dto.kakaopay.*;
import know_wave.comma.payment.entity.PaymentType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;

@Service
@RequiredArgsConstructor
public class KakaoPayClient implements PaymentClient<KakaopayReadyRequest, KakaopayApproveRequest, KakaopayRefundRequest> {

    private static final PaymentType PAYMENT_TYPE = PaymentType.KAKAO_PAY;

    @Value("${kakao.api.pay.ready-request-url}")
    private String readyUrl;

    @Value("${kakao.api.pay.approve-request-url}")
    private String approveUrl;

    @Value("${kakao.api.pay.cancel-request-url}")
    private String cancelUrl;

    @Value("${kakao.api.key}")
    private String apiKey;

    private static String authHeader = "Authorization";
    private static String authHeaderPrefix = "KakaoAK ";

    @Override
    public PaymentClientReadyResponse ready(KakaopayReadyRequest request) {
        RestClient restClient = RestClient.create();

        KakaopayReadyResponse response = restClient.post()
                .uri(readyUrl)
                .contentType(APPLICATION_FORM_URLENCODED)
                .header(authHeader, authHeaderPrefix + apiKey)
                .body(request)
                .retrieve()
                .body(KakaopayReadyResponse.class);

        return PaymentClientReadyResponse.of(
                response.getNext_redirect_mobile_url(),
                response.getNext_redirect_pc_url(),
                response.getTid());
    }

    @Override
    public PaymentClientApproveResponse approve(KakaopayApproveRequest request) {
        RestClient restClient = RestClient.create();

        KakaopayApproveResponse response = restClient.post()
                .uri(approveUrl)
                .contentType(APPLICATION_FORM_URLENCODED)
                .header(authHeader, authHeaderPrefix + apiKey)
                .body(request)
                .retrieve()
                .body(KakaopayApproveResponse.class);

        return PaymentClientApproveResponse.of(
                response.getTid(), response.getCid(), response.getPartner_order_id(),
                response.getPartner_user_id(), response.getAmount(), response.getQuantity(),
                response.getItem_name(), response.getCreated_at(), response.getApproved_at());
    }

    @Override
    public PaymentClientRefundResponse refund(KakaopayRefundRequest request) {
        RestClient restClient = RestClient.create();

        KakaopayRefundResponse response = restClient.post()
                .uri(cancelUrl)
                .contentType(APPLICATION_FORM_URLENCODED)
                .header(authHeader, authHeaderPrefix + apiKey)
                .body(request)
                .retrieve()
                .body(KakaopayRefundResponse.class);

        return PaymentClientRefundResponse.of(
                response.getTid(), response.getCid(), response.getPartner_order_id(),
                response.getPartner_user_id(), response.getStatus(), response.getAmount(),
                response.getApproved_cancel_amount(), response.getQuantity(), response.getItem_name(),
                response.getCreated_at(), response.getApproved_at(), response.getCanceled_at());
    }

    @Override
    public boolean isSupport(PaymentType type) {
        return type == PAYMENT_TYPE;
    }

    @Override
    public PaymentType getPaymentType() {
        return PAYMENT_TYPE;
    }
}
