package com.know_wave.comma.comma_backend.payment.dto.kakao;

import com.know_wave.comma.comma_backend.order.dto.OrderInfoDto;
import com.know_wave.comma.comma_backend.payment.dto.PaymentPrepareDto;
import com.know_wave.comma.comma_backend.payment.service.CommaArduinoDepositPolicy;
import com.know_wave.comma.comma_backend.payment.service.PaymentGateway;
import lombok.Getter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Getter
public class KakaoPayReadyRequest {

    private final MultiValueMap<String, Object> value = new LinkedMultiValueMap<>();

    public static KakaoPayReadyRequest of(String idempotencyKey, String paymentRequestId, PaymentPrepareDto paymentPrepareDto, String accountId, String cid, CommaArduinoDepositPolicy depositPolicy, OrderInfoDto orderInfoDto) {
        return new KakaoPayReadyRequest(
                idempotencyKey,
                paymentRequestId,
                cid,
                orderInfoDto.subject().name(),
                paymentPrepareDto.tempOrderId(),
                accountId,
                depositPolicy.getProductName(),
                depositPolicy.getAmount(),
                depositPolicy.getTaxFreeAmount(),
                null
        );
    }

    public static KakaoPayReadyRequest ofWithSSE(String idempotencyKey, String paymentRequestId, PaymentPrepareDto paymentPrepareDto, String accountId, String cid, CommaArduinoDepositPolicy depositPolicy, OrderInfoDto orderInfoDto, String sseId) {
        return new KakaoPayReadyRequest(
                idempotencyKey,
                paymentRequestId,
                cid,
                orderInfoDto.subject().name(),
                paymentPrepareDto.tempOrderId(),
                accountId,
                depositPolicy.getProductName(),
                depositPolicy.getAmount(),
                depositPolicy.getTaxFreeAmount(),
                sseId
        );
    }
    
    private KakaoPayReadyRequest(String idempotencyKey, String paymentRequestId, String cid, String subject, String tempOrderId, String accountId, String itemName, int totalAmount, int taxFreeAmount, String sseId) {
        if (sseId.isEmpty()) {
            sseId = "testSSEId";
        }
        value.add("cid", cid);
        value.add("partner_order_id", tempOrderId);
        value.add("partner_user_id", accountId);
        value.add("item_name", itemName);
        value.add("quantity", 1);
        value.add("total_amount", totalAmount);
        value.add("tax_free_amount", taxFreeAmount);
        value.add("approval_url", PaymentGateway.successUrl.formatted("kakao", paymentRequestId, idempotencyKey, accountId, tempOrderId, subject, sseId));
        value.add("fail_url", PaymentGateway.failUrl.formatted("kakao", paymentRequestId, idempotencyKey, tempOrderId, sseId));
        value.add("cancel_url", PaymentGateway.cancelUrl.formatted("kakao", paymentRequestId, idempotencyKey, tempOrderId, sseId));
    }

}
