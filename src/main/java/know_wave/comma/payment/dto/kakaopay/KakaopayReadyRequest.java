package know_wave.comma.payment.dto.kakaopay;

import know_wave.comma.payment.dto.gateway.PaymentGatewayCheckoutRequest;
import know_wave.comma.payment.entity.PaymentCbUrl;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Getter
@RequiredArgsConstructor
public class KakaopayReadyRequest {

    public static KakaopayReadyRequest of(Map<String, String> callbackUrlMap, PaymentGatewayCheckoutRequest checkoutDto, String paymentRequestId) {
        String accountId = checkoutDto.getAccount().getId();
        String paymentType = checkoutDto.getPaymentType().getType();
        String orderNumber = checkoutDto.getOrderNumber();
        String feature = checkoutDto.getPaymentFeature().getFeature();

        return new KakaopayReadyRequest(
                callbackUrlMap.get(PaymentCbUrl.CID_KEY),
                paymentRequestId,
                accountId,
                feature,
                checkoutDto.getQuantity(),
                checkoutDto.getAmount(),
                0,
                callbackUrlMap.get(PaymentCbUrl.SUCCESS_CB_URL_KEY).formatted(paymentType, feature, accountId, paymentRequestId, orderNumber),
                callbackUrlMap.get(PaymentCbUrl.CANCEL_CB_URL_KEY).formatted(paymentType, accountId, feature, paymentRequestId, orderNumber),
                callbackUrlMap.get(PaymentCbUrl.FAIL_CB_URL_KEY).formatted(paymentType, accountId, feature, paymentRequestId, orderNumber)
        );
    }

    private final String cid;
    private final String partnerOrderId;
    private final String partnerUserId;
    private final String itemName;
    private final int quantity;
    private final int totalAmount;
    private final int taxFreeAmount;
    private final String approvalUrl;
    private final String cancelUrl;
    private final String failUrl;
}
