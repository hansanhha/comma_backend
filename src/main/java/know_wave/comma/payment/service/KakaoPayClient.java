package know_wave.comma.payment.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import know_wave.comma.payment.dto.client.PaymentClientApproveResponse;
import know_wave.comma.payment.dto.client.PaymentClientReadyResponse;
import know_wave.comma.payment.dto.client.PaymentClientRefundResponse;
import know_wave.comma.payment.dto.kakaopay.*;
import know_wave.comma.payment.entity.PaymentType;
import know_wave.comma.payment.exception.PaymentClient4xxException;
import know_wave.comma.payment.exception.PaymentClient5xxException;
import know_wave.comma.payment.exception.PaymentClientUnknownException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;
import static org.springframework.http.MediaType.APPLICATION_JSON;

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

    private RestClient restClient;

    @PostConstruct
    public void restClientInit() {
        restClient = RestClient.builder()
                .defaultHeaders(httpHeaders -> {
                    httpHeaders.setContentType(APPLICATION_FORM_URLENCODED);
                    httpHeaders.setAccept(List.of(APPLICATION_JSON));
                    httpHeaders.set(HttpHeaders.AUTHORIZATION, authHeaderPrefix + apiKey);
                })
                .build();
    }

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String authHeaderPrefix = "KakaoAK ";

    @Override
    public PaymentClientReadyResponse ready(KakaopayReadyRequest kakaopayReadyRequest) {
        KakaopayReadyResponse readyResponse = restClient.post()
                .uri(readyUrl)
                .body(kakaopayReadyRequest.getBody())
                .exchange(exchangeCallback(KakaopayReadyResponse.class));

        return PaymentClientReadyResponse.create(
                readyResponse.getNext_redirect_mobile_url(),
                readyResponse.getNext_redirect_pc_url(),
                readyResponse.getTid());
    }

    @Override
    public PaymentClientApproveResponse approve(KakaopayApproveRequest kakaopayApproveRequest) {
        KakaopayApproveResponse response = restClient.post()
                .uri(approveUrl)
                .body(kakaopayApproveRequest.getBody())
                .exchange(exchangeCallback(KakaopayApproveResponse.class));

        return PaymentClientApproveResponse.create(
                response.getTid(), response.getCid(), response.getPartner_order_id(),
                response.getPartner_user_id(), response.getAmount(), response.getQuantity(),
                response.getItem_name(), LocalDateTime.ofInstant(response.getCreated_at().toInstant(), ZoneId.systemDefault()), LocalDateTime.ofInstant(response.getApproved_at().toInstant(), ZoneId.systemDefault()));
    }

    @Override
    public PaymentClientRefundResponse refund(KakaopayRefundRequest kakaopayRefundRequestequest) {
        KakaopayRefundResponse response = restClient.post()
                .uri(cancelUrl)
                .body(kakaopayRefundRequestequest.getBody())
                .exchange(exchangeCallback(KakaopayRefundResponse.class));

        return PaymentClientRefundResponse.create(
                response.getTid(), response.getCid(), response.getPartner_order_id(),
                response.getPartner_user_id(), response.getStatus(), response.getAmount(),
                response.getApproved_cancel_amount(), response.getQuantity(), response.getItem_name(),
                LocalDateTime.ofInstant(response.getCreated_at().toInstant(), ZoneId.systemDefault()),
                LocalDateTime.ofInstant(response.getApproved_at().toInstant(), ZoneId.systemDefault()),
                LocalDateTime.ofInstant(response.getCanceled_at().toInstant(), ZoneId.systemDefault()));
    }

    private <T> RestClient.RequestHeadersSpec.ExchangeFunction<T> exchangeCallback(Class<T> clazz) {
        return (request, response) -> {
            if (response.getStatusCode().is2xxSuccessful()) {
                return convertResponse(response.getBody(), clazz);
            } else if (response.getStatusCode().is4xxClientError()) {
                PaymentErrorResponse paymentErrorResponse = convertResponse(response.getBody(), PaymentErrorResponse.class);
                throw new PaymentClient4xxException(paymentErrorResponse.getMsg(), response.getStatusCode(), paymentErrorResponse.getCode());
            } else if (response.getStatusCode().is5xxServerError()) {
                PaymentErrorResponse paymentErrorResponse = convertResponse(response.getBody(), PaymentErrorResponse.class);
                throw new PaymentClient5xxException(paymentErrorResponse.getMsg(), response.getStatusCode(), paymentErrorResponse.getCode());
            } else {
                throw new PaymentClientUnknownException();
            }
        };
    }

    @Getter
    private static class PaymentErrorResponse {
        private String msg;
        private int code;
    }

    private <T> T convertResponse(InputStream object, Class<T> clazz) throws IOException {
        return objectMapper.readValue(object, clazz);
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
