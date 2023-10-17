package com.know_wave.comma.comma_backend.payment.dto.kakao;

import com.know_wave.comma.comma_backend.payment.entity.Deposit;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class KakaoPayApproveRequest {

    private final Map<String, Object> value = new HashMap<>();

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
        value.put("cid", cid);
        value.put("tid", transactionId);
        value.put("partner_order_id", orderId);
        value.put("partner_user_id", accountId);
        value.put("pg_token", paymentToken);
    }
}
