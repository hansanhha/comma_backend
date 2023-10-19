package com.know_wave.comma.comma_backend.payment.service;

import com.know_wave.comma.comma_backend.account.service.normal.AccountQueryService;
import com.know_wave.comma.comma_backend.order.dto.OrderInfoDto;
import com.know_wave.comma.comma_backend.payment.dto.PaymentPrepareResult;
import com.know_wave.comma.comma_backend.payment.dto.PaymentPrepareDto;
import com.know_wave.comma.comma_backend.payment.dto.PaymentRefundResult;
import com.know_wave.comma.comma_backend.payment.dto.WebEntityCreator;
import com.know_wave.comma.comma_backend.payment.dto.kakao.*;
import com.know_wave.comma.comma_backend.payment.entity.Deposit;
import com.know_wave.comma.comma_backend.payment.entity.PaymentType;
import com.know_wave.comma.comma_backend.util.GenerateUtils;
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

    private static final String paymentReadyUrl = "https://kapi.kakao.com/v1/payment/ready";
    private static final String paymentApproveUrl = "https://kapi.kakao.com/v1/payment/approve";
    private static final String paymentCancelUrl = "https://kapi.kakao.com/v1/payment/cancel";

    public KakaoPayService(@Qualifier("kakaoPayApiClient") RestTemplate kakaoPayApiClient, CommaArduinoDepositPolicy depositPolicy) {
        this.kakaoPayApiClient = kakaoPayApiClient;
        this.depositPolicy = depositPolicy;
    }

    @Override
    public PaymentPrepareResult ready(String idempotencyKey, PaymentPrepareDto paymentPrepareDto, OrderInfoDto orderInfoDto) {
        String paymentRequestId = GenerateUtils.generatedRandomCode();

        String accountId = AccountQueryService.getAuthenticatedId();
        var readyRequest = KakaoPayReadyRequest.of(idempotencyKey, paymentRequestId, paymentPrepareDto, accountId, cid, depositPolicy, orderInfoDto);
        var httpEntity = WebEntityCreator.toPaymentEntity(readyRequest.getValue());

        var kakaoPayReadyResponse = kakaoPayApiClient.postForObject(
                paymentReadyUrl,
                httpEntity,
                KakaoPayReadyResponse.class);

        return PaymentPrepareResult.of(Objects.requireNonNull(kakaoPayReadyResponse), paymentRequestId);
    }

    @Override
    public PaymentPrepareResult readyWithSSE(String idempotencyKey, PaymentPrepareDto paymentPrepareDto, OrderInfoDto orderInfoDto, String sseId) {
        String paymentRequestId = GenerateUtils.generatedRandomCode();

        String accountId = AccountQueryService.getAuthenticatedId();
        var readyRequest = KakaoPayReadyRequest.ofWithSSE(idempotencyKey, paymentRequestId, paymentPrepareDto, accountId, cid, depositPolicy, orderInfoDto, sseId);
        var httpEntity = WebEntityCreator.toPaymentEntity(readyRequest.getValue());

        var kakaoPayReadyResponse = kakaoPayApiClient.postForObject(
                paymentReadyUrl,
                httpEntity,
                KakaoPayReadyResponse.class);

        return PaymentPrepareResult.of(Objects.requireNonNull(kakaoPayReadyResponse), paymentRequestId);
    }

    @Override
    public void pay(Deposit deposit, String tempOrderNumber, String paymentToken) {
        var approveRequest = KakaoPayApproveRequest.of(cid, deposit, tempOrderNumber, paymentToken);

        var httpEntity = WebEntityCreator.toPaymentEntity(approveRequest.getValue());

        var response = kakaoPayApiClient.postForEntity(
                paymentApproveUrl,
                httpEntity,
                KakaoPayApproveResponse.class);
    }

    @Override
    public PaymentRefundResult refund(Deposit deposit) {
        var cancelRequest = KaKaoPayCancelRequest.of(cid, deposit);

        var httpEntity = WebEntityCreator.toPaymentEntity(cancelRequest.getValue());

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
