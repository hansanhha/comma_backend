package know_wave.comma.payment.dto.gateway;

import know_wave.comma.payment.entity.PaymentFeature;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;

@Getter
@RequiredArgsConstructor
public class ErrorCallback {

    public static ErrorCallback create(String paymentRequestId, String orderNumber, String accountId, PaymentFeature paymentFeature, HttpStatusCode statusCode, int errorCode, String errorMessage) {
        return new ErrorCallback(paymentRequestId, orderNumber, accountId, paymentFeature, statusCode, errorCode, errorMessage);
    }
    private final String paymentRequestId;
    private final String orderNumber;
    private final String accountId;
    private final PaymentFeature paymentFeature;
    private final HttpStatusCode statusCode;
    private final int errorCode;
    private final String errorMessage;
}
