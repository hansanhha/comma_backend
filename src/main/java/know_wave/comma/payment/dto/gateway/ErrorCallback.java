package know_wave.comma.payment.dto.gateway;

import know_wave.comma.payment.entity.PaymentFeature;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ErrorCallback {

    public static ErrorCallback create(String paymentRequestId, String orderNumber, String accountId, PaymentFeature paymentFeature, String errorMessage) {
        return new ErrorCallback(paymentRequestId, orderNumber, accountId, paymentFeature, errorMessage);
    }

    private final String paymentRequestId;
    private final String orderNumber;
    private final String accountId;
    private final PaymentFeature paymentFeature;
    private final String errorMessage;
}
