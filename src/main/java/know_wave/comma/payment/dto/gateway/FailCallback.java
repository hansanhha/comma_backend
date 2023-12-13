package know_wave.comma.payment.dto.gateway;

import know_wave.comma.payment.entity.PaymentFeature;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FailCallback {

    public static FailCallback of(String paymentRequestId, String orderNumber, String accountId, PaymentFeature paymentFeature) {
        return new FailCallback(paymentRequestId, orderNumber, accountId, paymentFeature);
    }

    private final String paymentRequestId;
    private final String orderNumber;
    private final String accountId;
    private final PaymentFeature paymentFeature;
}
