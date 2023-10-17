package com.know_wave.comma.comma_backend.payment.dto.kakao;

import com.know_wave.comma.comma_backend.payment.dto.PaymentAuthRequest;
import com.know_wave.comma.comma_backend.payment.service.CommaArduinoDepositPolicy;
import com.know_wave.comma.comma_backend.payment.service.PaymentGateway;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class KakaoPayReadyRequest {

    private final Map<String, Object> value = new HashMap<>();

    public static KakaoPayReadyRequest of(PaymentAuthRequest request, String cid, CommaArduinoDepositPolicy depositPolicy) {
        return new KakaoPayReadyRequest(
                cid,
                request.arduinoOrderId(),
                request.accountId(),
                depositPolicy.getProductName(),
                depositPolicy.getAmount(),
                depositPolicy.getTaxFreeAmount()
                );
    }

    private KakaoPayReadyRequest(String cid, String orderId, String accountId, String itemName, int totalAmount, int taxFreeAmount) {
        value.put("cid", cid);
        value.put("partner_order_id", orderId);
        value.put("partner_user_id", accountId);
        value.put("item_name", itemName);
        value.put("quantity", 1);
        value.put("total_amount", totalAmount);
        value.put("tax_free_amount", taxFreeAmount);
        value.put("approval_url", PaymentGateway.successUrl.formatted("kakao", orderId, accountId));
        value.put("fail_url", PaymentGateway.failUrl.formatted("kakao", orderId, accountId));
        value.put("cancel_url", PaymentGateway.cancelUrl.formatted("kakao", orderId, accountId));
    }

}
