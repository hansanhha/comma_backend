package com.know_wave.comma.comma_backend.payment.service;

import com.know_wave.comma.comma_backend.payment.dto.*;
import com.know_wave.comma.comma_backend.payment.dto.kakao.*;
import com.know_wave.comma.comma_backend.payment.entity.Deposit;
import com.know_wave.comma.comma_backend.payment.entity.PaymentType;
import com.know_wave.comma.comma_backend.util.GenerateUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class KakaoPayService implements PaymentService {

    private final RestTemplate kakaoPayApiClient;

    private final CommaArduinoDepositPolicy depositPolicy;

    @Value("${kakao.api.customer.id}")
    private String cid;

    private static final String paymentReadyUrl = "https://kapi.kakao.com/v1/payment/ready";
    private static final String paymentApproveUrl = "https://kapi.kakao.com/v1/payment/approve";
    private static final String paymentCancelUrl = "https://kapi.kakao.com/v1/payment/cancel";

    public KakaoPayService(@Qualifier("kakaoPayApiClient") RestTemplate kakaoPayApiClient, CommaArduinoDepositPolicy depositPolicy) {
        this.kakaoPayApiClient = kakaoPayApiClient;
        this.depositPolicy = depositPolicy;
    }

    @Override
    public PaymentAuthResult ready(String idempotencyKey, PaymentAuthRequest request) {
        String paymentRequestId = GenerateUtils.generatedCodeWithDate();

        var readyRequest = KakaoPayReadyRequest.of(idempotencyKey, paymentRequestId, request, cid, depositPolicy);
        var httpEntity = HttpEntityCreator.of(readyRequest.getValue());

        var kakaoPayReadyResponse = kakaoPayApiClient.postForObject(
                paymentReadyUrl,
                httpEntity,
                KakaoPayReadyResponse.class);

        return PaymentAuthResult.of(Objects.requireNonNull(kakaoPayReadyResponse), paymentRequestId);
    }

    @Override
    public void pay(Deposit deposit, String paymentToken) {
        var approveRequest = KakaoPayApproveRequest.of(cid, deposit, paymentToken);

        var httpEntity = HttpEntityCreator.of(approveRequest.getValue());

        var response = kakaoPayApiClient.postForEntity(
                paymentApproveUrl,
                httpEntity,
                KakaoPayApproveResponse.class);
    }

    @Override
    public PaymentRefundResult refund(Deposit deposit) {
        var cancelRequest = KaKaoPayCancelRequest.of(cid, deposit);

        var httpEntity = HttpEntityCreator.of(cancelRequest.getValue());

        var response = kakaoPayApiClient.postForEntity(
                paymentCancelUrl,
                httpEntity,
                KaKaoPayCancelResponse.class);

        return PaymentRefundResult.of(Objects.requireNonNull(response.getBody()).canceled_at());
    }

    @Override
    public void cancel(Deposit request) {

    }

    @Override
    public boolean supports(PaymentType type) {
        return type == PaymentType.KAKAO;
    }

}
