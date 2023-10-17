package com.know_wave.comma.comma_backend.payment.dto.kakao;

import com.know_wave.comma.comma_backend.payment.dto.PaymentRequest;
import com.know_wave.comma.comma_backend.payment.service.CommaArduinoDepositPolicy;
import com.know_wave.comma.comma_backend.payment.service.PaymentGateway;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class KaKaoPaymentReady {

    private final Map<String, Object> value = new HashMap<>();

    public static KaKaoPaymentReady of(PaymentRequest request, String cid, CommaArduinoDepositPolicy depositPolicy) {
        return new KaKaoPaymentReady(
                cid,
                request.arduinoOrderId(),
                request.accountId(),
                depositPolicy.getProductName(),
                depositPolicy.getAmount(),
                depositPolicy.getTaxFreeAmount()
                );
    }

    public void applyAmount(int amount, int taxFreeAmount) {
        value.put("total_amount", amount);
        value.put("tax_free_amount", taxFreeAmount);
    }

    private KaKaoPaymentReady(String cid, String orderId, String accountId, String itemName, int totalAmount, int taxFreeAmount) {
        value.put("cid", cid);
        value.put("partner_order_id", orderId);
        value.put("partner_user_id", accountId);
        value.put("item_name", itemName);
        value.put("quantity", 1);
        value.put("total_amount", totalAmount);
        value.put("tax_free_amount", taxFreeAmount);
        value.put("approval_url", PaymentGateway.successUrl);
        value.put("fail_url", PaymentGateway.failUrl);
        value.put("cancel_url", PaymentGateway.cancelUrl);
    }

}
