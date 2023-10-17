package com.know_wave.comma.comma_backend.payment.service;

import com.know_wave.comma.comma_backend.payment.dto.*;
import com.know_wave.comma.comma_backend.payment.dto.kakao.KakaoPayApproveRequest;
import com.know_wave.comma.comma_backend.payment.dto.kakao.KakaoPayApproveResponse;
import com.know_wave.comma.comma_backend.payment.dto.kakao.KakaoPayReadyRequest;
import com.know_wave.comma.comma_backend.payment.dto.kakao.KakaoPayReadyResponse;
import com.know_wave.comma.comma_backend.payment.entity.Deposit;
import com.know_wave.comma.comma_backend.payment.entity.PaymentType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
public class KakaoPayService implements PaymentService {

    private final RestTemplate kakaoPayApiClient;

    private final CommaArduinoDepositPolicy depositPolicy;

    @Value("${kakao.api.customer.id}")
    private String cid;

    private static final String paymentReadyUri = "https://kapi.kakao.com/v1/payment/ready";
    private static final String paymentApproveUri = "https://kapi.kakao.com/v1/payment/approve";

    public KakaoPayService(@Qualifier("kakaoPayApiClient") RestTemplate kakaoPayApiClient, CommaArduinoDepositPolicy depositPolicy) {
        this.kakaoPayApiClient = kakaoPayApiClient;
        this.depositPolicy = depositPolicy;
    }

    @Override
    public PaymentAuthResult ready(PaymentAuthRequest request) {
        var kakaoPayReadyRequest = KakaoPayReadyRequest.of(request, cid, depositPolicy);

        var response = kakaoPayApiClient.postForEntity(
                paymentReadyUri,
                kakaoPayReadyRequest.getValue(),
                KakaoPayReadyResponse.class);

        return PaymentAuthResult.of(Objects.requireNonNull(response.getBody()));
    }

    @Override
    public void pay(Deposit deposit, String paymentToken) {
        var kakaoPayApproveRequest = KakaoPayApproveRequest.of(cid, deposit, paymentToken);

        var response = kakaoPayApiClient.postForEntity(
                paymentApproveUri,
                null,
                KakaoPayApproveResponse.class,
                kakaoPayApproveRequest.getValue());
    }

    @Override
    public void refund(PaymentAuthRequest request) {

    }

    @Override
    public void cancel(PaymentAuthRequest request) {

    }

    @Override
    public boolean supports(PaymentType type) {
        return type == PaymentType.KAKAO;
    }

}
