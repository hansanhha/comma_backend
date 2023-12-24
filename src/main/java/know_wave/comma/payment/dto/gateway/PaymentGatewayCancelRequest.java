package know_wave.comma.payment.dto.gateway;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PaymentGatewayCancelRequest {

    public static PaymentGatewayCancelRequest create(String paymentRequestId, String orderNumber, String accountId, String paymentType, String paymentFeature) {
        return new PaymentGatewayCancelRequest(paymentRequestId, orderNumber, accountId, paymentType, paymentFeature);
    }

    private final String paymentRequestId;
    private final String orderNumber;
    private final String accountId;
    private final String paymentType;
    private final String paymentFeature;

}
