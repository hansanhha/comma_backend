package com.know_wave.comma.comma_backend.payment.service;

import com.know_wave.comma.comma_backend.payment.dto.*;
import com.know_wave.comma.comma_backend.payment.dto.kakao.KaKaoPaymentReady;
import com.know_wave.comma.comma_backend.payment.dto.kakao.KaKaoPaymentReadyResponse;
import com.know_wave.comma.comma_backend.payment.entity.PaymentType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
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

    public KakaoPayService(@Qualifier("kakaoPayApiClient") RestTemplate kakaoPayApiClient, CommaArduinoDepositPolicy depositPolicy) {
        this.kakaoPayApiClient = kakaoPayApiClient;
        this.depositPolicy = depositPolicy;
    }

    @Override
    public PaymentWebResult ready(PaymentRequest request) {
        var kaKaoPaymentReady = KaKaoPaymentReady.of(request, cid, depositPolicy);

        ResponseEntity<KaKaoPaymentReadyResponse> response = kakaoPayApiClient.postForEntity(
                paymentReadyUri,
                null,
                KaKaoPaymentReadyResponse.class,
                kaKaoPaymentReady.getValue());

        return PaymentWebResult.ofReady(Objects.requireNonNull(response.getBody()));
    }

    @Override
    public void pay(PaymentRequest request) {

    }

    @Override
    public void refund(PaymentRequest request) {

    }

    @Override
    public void cancel(PaymentRequest request) {

    }

    @Override
    public boolean supports(PaymentType type) {
        return type == PaymentType.KAKAO_PAY;
    }

}
