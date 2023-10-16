package com.know_wave.comma.comma_backend.payment.service;

import com.know_wave.comma.comma_backend.payment.dto.KaKaoPayReady;
import com.know_wave.comma.comma_backend.payment.dto.KaKaoPayReadyResponse;
import com.know_wave.comma.comma_backend.payment.dto.PaymentInfo;
import com.know_wave.comma.comma_backend.payment.dto.PaymentResult;
import com.know_wave.comma.comma_backend.payment.entity.PaymentType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class KakaoPayService implements PaymentService<PaymentInfo, PaymentResult> {

    @Qualifier("kakaoPayApiClient")
    private final RestTemplate kakaoPayApiClient;

    @Value("${kakao.api.customer.id}")
    private String cid;

    private final String paymentRequestUri = "https://kapi.kakao.com/v1/payment/ready";

    public KakaoPayService(RestTemplate kakaoPayApiClient) {
        this.kakaoPayApiClient = kakaoPayApiClient;
    }

    @Override
    public PaymentResult ready(PaymentInfo paymentInfo) {
        var param = getPaymentReadyRequestBody(paymentInfo);

        ResponseEntity<KaKaoPaymentReadyResponse> response = kakaoPayApiClient.postForEntity(
                paymentRequestUri,
                null,
                KaKaoPaymentReadyResponse.class,
                param);

        return PaymentInfo.of(response.getBody());
    }

    @Override
    public PaymentResult pay(PaymentInfo paymentInfo) {
        return null;
    }

    @Override
    public PaymentResult refund(PaymentInfo paymentInfo) {
        return null;
    }

    @Override
    public PaymentResult cancel(PaymentInfo paymentInfo) {
        return null;
    }

    @Override
    public boolean supports(PaymentType type) {
        return type == PaymentType.KAKAO_PAY;
    }

    private Map<String, Object> getPaymentReadyRequestBody(KaKaoPayReady paymentInfo) {
        HashMap<String, Object> param = new HashMap<>();

        param.put("cid", cid);
        param.put("partner_order_id", paymentInfo.getOrderId());
        param.put("partner_user_id", paymentInfo.getAccountId());
        param.put("item_name", paymentInfo.getItemName());
        param.put("quantity", 1);
        param.put("total_amount", paymentInfo.getTotalAmount());
        param.put("tax_free_amount", paymentInfo.getTaxFreeAmount());
        param.put("approval_url", PaymentGateway.successUrl);
        param.put("fail_url", PaymentGateway.failUrl);
        param.put("cancel_url", PaymentGateway.cancelUrl);

        return param;
    }
}
