package know_wave.comma.payment.exception;

import org.springframework.http.HttpStatusCode;

public class PaymentClientUnknownException extends PaymentClientException {

    public PaymentClientUnknownException() {
        super();
    }

    public PaymentClientUnknownException(String message) {
        super(message);
    }

    public PaymentClientUnknownException(String message, HttpStatusCode httpStatusCode, int errorCode) {
        super(message, httpStatusCode, errorCode);
    }
}
