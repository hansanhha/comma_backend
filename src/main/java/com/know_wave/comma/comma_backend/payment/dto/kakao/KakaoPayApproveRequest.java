package com.know_wave.comma.comma_backend.payment.dto.kakao;

import com.know_wave.comma.comma_backend.payment.entity.Deposit;
import lombok.Getter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.Map;

@Getter
public class KakaoPayApproveRequest {

    private final MultiValueMap<String, Object> value = new LinkedMultiValueMap<>();

    public static KakaoPayApproveRequest of(String cid, Deposit deposit, String paymentToken) {
        return new KakaoPayApproveRequest(
                cid,
                deposit.getPaymentTransactionId(),
                deposit.getOrderInfo().getId(),
                deposit.getAccount().getId(),
                paymentToken
        );
    }

    private KakaoPayApproveRequest(String cid, String transactionId, String orderId, String accountId, String paymentToken) {
        value.add("cid", cid);
        value.add("tid", transactionId);
        value.add("partner_order_id", orderId);
        value.add("partner_user_id", accountId);
        value.add("pg_token", paymentToken);
    }
}
