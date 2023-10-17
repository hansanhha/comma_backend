package com.know_wave.comma.comma_backend.payment.dto.kakao;

import com.know_wave.comma.comma_backend.payment.dto.PaymentAuthRequest;
import com.know_wave.comma.comma_backend.payment.service.CommaArduinoDepositPolicy;
import com.know_wave.comma.comma_backend.payment.service.PaymentGateway;
import lombok.Getter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.Map;

@Getter
public class KakaoPayReadyRequest {

    private final MultiValueMap<String, Object> value = new LinkedMultiValueMap<>();
    
    public static KakaoPayReadyRequest of(String paymentRequestId, PaymentAuthRequest request, String cid, CommaArduinoDepositPolicy depositPolicy) {
        return new KakaoPayReadyRequest(
                paymentRequestId,
                cid,
                request.arduinoOrderId(),
                request.accountId(),
                depositPolicy.getProductName(),
                depositPolicy.getAmount(),
                depositPolicy.getTaxFreeAmount()
        );
    }
    
    private KakaoPayReadyRequest(String paymentRequestId, String cid, String orderId, String accountId, String itemName, int totalAmount, int taxFreeAmount) {
        value.add("cid", cid);
        value.add("partner_order_id", orderId);
        value.add("partner_user_id", accountId);
        value.add("item_name", itemName);
        value.add("quantity", 1);
        value.add("total_amount", totalAmount);
        value.add("tax_free_amount", taxFreeAmount);
        value.add("approval_url", PaymentGateway.successUrl.formatted("kakao", paymentRequestId));
        value.add("fail_url", PaymentGateway.failUrl.formatted("kakao", paymentRequestId));
        value.add("cancel_url", PaymentGateway.cancelUrl.formatted("kakao", paymentRequestId));
    }

}
