package know_wave.comma.payment.exception;

import know_wave.comma.payment.entity.PaymentFeature;
import org.springframework.http.HttpStatusCode;

public class PaymentRefundException extends PaymentException {

    private String paymentRequestId;
    private String accountId;
    private PaymentFeature paymentFeature;
    private HttpStatusCode statusCode;
    private int errorCode;
    private String errorMessage;

    public PaymentRefundException() {
        super();
    }

    public PaymentRefundException(String message) {
        super(message);
    }

    public PaymentRefundException(String paymentRequestId, String accountId, PaymentFeature paymentFeature, HttpStatusCode statusCode, int errorCode, String errorMessage) {
        super();
        this.accountId = accountId;
        this.paymentRequestId = paymentRequestId;
        this.paymentFeature = paymentFeature;
        this.statusCode = statusCode;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
