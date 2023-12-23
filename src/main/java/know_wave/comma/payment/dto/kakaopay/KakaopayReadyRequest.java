package know_wave.comma.payment.dto.kakaopay;

import know_wave.comma.payment.dto.gateway.PaymentGatewayCheckoutRequest;
import know_wave.comma.payment.entity.PaymentCbUrl;
import lombok.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Collections;
import java.util.Map;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class KakaopayReadyRequest {

    public static KakaopayReadyRequest create(Map<String, String> paymentCbUrl, PaymentGatewayCheckoutRequest checkoutDto, String paymentRequestId) {
        String accountId = checkoutDto.getAccount().getId();
        String paymentType = checkoutDto.getPaymentType().name().toLowerCase();
        String orderNumber = checkoutDto.getOrderNumber();
        String feature = checkoutDto.getPaymentFeature().name().toLowerCase();

        MultiValueMap<String, Object> requestMap = new LinkedMultiValueMap<>();

        requestMap.put("cid", Collections.singletonList(paymentCbUrl.get(PaymentCbUrl.CID_KEY)));
        requestMap.put("partner_order_id", Collections.singletonList(paymentRequestId));
        requestMap.put("partner_user_id", Collections.singletonList(accountId));
        requestMap.put("item_name", Collections.singletonList("컴마 실습재료 주문 보증금"));
        requestMap.put("quantity", Collections.singletonList(String.valueOf(checkoutDto.getQuantity())));
        requestMap.put("total_amount", Collections.singletonList(String.valueOf(checkoutDto.getAmount())));
        requestMap.put("tax_free_amount", Collections.singletonList(0));
        requestMap.put("approval_url", Collections.singletonList(paymentCbUrl.get(PaymentCbUrl.SUCCESS_CB_URL_KEY).formatted(paymentType, feature, accountId, paymentRequestId, orderNumber)));
        requestMap.put("cancel_url", Collections.singletonList(paymentCbUrl.get(PaymentCbUrl.CANCEL_CB_URL_KEY).formatted(paymentType, accountId, feature, paymentRequestId, orderNumber)));
        requestMap.put("fail_url", Collections.singletonList(paymentCbUrl.get(PaymentCbUrl.FAIL_CB_URL_KEY).formatted(paymentType, accountId, feature, paymentRequestId, orderNumber)));

        return new KakaopayReadyRequest(requestMap);
    }

    private final MultiValueMap<String, Object> body;
}
