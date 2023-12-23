package know_wave.comma.payment.dto.gateway;

import know_wave.comma.payment.entity.PaymentFeature;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CompleteCallback {

    public static CompleteCallback create(String paymentRequestId, String orderNumber, String accountId, PaymentFeature paymentFeature) {
        return new CompleteCallback(paymentRequestId, orderNumber, accountId, paymentFeature);
    }

    private final String paymentRequestId;
    private final String orderNumber;
    private final String accountId;
    private final PaymentFeature paymentFeature;

}
