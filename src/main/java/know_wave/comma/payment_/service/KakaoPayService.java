package know_wave.comma.payment_.service;

import know_wave.comma.account.service.normal.AccountQueryService;
import know_wave.comma.order_.dto.OrderInfoDto;
import know_wave.comma.payment_.dto.PaymentPrepareDto;
import know_wave.comma.payment_.dto.PaymentPrepareResult;
import know_wave.comma.payment_.dto.PaymentRefundResult;
import know_wave.comma.payment_.dto.WebEntityCreator;
import know_wave.comma.payment_.dto.kakao.*;
import know_wave.comma.payment_.entity.Deposit;
import know_wave.comma.payment_.entity.PaymentType;
import know_wave.comma.common.util.GenerateUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
public class KakaoPayService implements PaymentService {

    private final AccountQueryService accountQueryService;
    private final RestTemplate kakaoPayApiClient;

    private final CommaArduinoDepositPolicy depositPolicy;

    @Value("${kakao.api.customer.id}")
    private String cid;

    private static final String paymentReadyUrl = "https://kapi.kakao.com/v1/payment/ready";
    private static final String paymentApproveUrl = "https://kapi.kakao.com/v1/payment/approve";
    private static final String paymentCancelUrl = "https://kapi.kakao.com/v1/payment/cancel";

    public KakaoPayService(@Qualifier("kakaoPayApiClient") RestTemplate kakaoPayApiClient, CommaArduinoDepositPolicy depositPolicy, AccountQueryService accountQueryService) {
        this.kakaoPayApiClient = kakaoPayApiClient;
        this.depositPolicy = depositPolicy;
        this.accountQueryService = accountQueryService;
    }

    @Override
    public PaymentPrepareResult ready(String idempotencyKey, PaymentPrepareDto paymentPrepareDto, OrderInfoDto orderInfoDto) {
        String paymentRequestId = GenerateUtils.generateRandomCode();

        String accountId = accountQueryService.getAuthenticatedId();
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
        String paymentRequestId = GenerateUtils.generateRandomCode();

        String accountId = accountQueryService.getAuthenticatedId();
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
