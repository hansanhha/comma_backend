package know_wave.comma.payment.dto.gateway;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Getter
@RequiredArgsConstructor
public class PaymentGatewayCancelResponse {

    public static PaymentGatewayCancelResponse create(Map<String, String> transactionResult, String paymentRequestId, String orderNumber, String accountId, int amount, int quantity, String paymentStatus, String paymentFeature, String paymentType) {
        return new PaymentGatewayCancelResponse(transactionResult, paymentRequestId, orderNumber, accountId, amount, quantity, paymentStatus, paymentFeature, paymentType);
    }

    private final Map<String, String> transactionResult;
    private final String paymentRequestId;
    private final String orderNumber;
    private final String accountId;
    private final int amount;
    private final int quantity;
    private final String paymentStatus;
    private final String paymentFeature;
    private final String paymentType;

}
