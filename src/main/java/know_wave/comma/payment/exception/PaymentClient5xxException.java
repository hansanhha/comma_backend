package know_wave.comma.payment.exception;

import org.springframework.http.HttpStatusCode;

public class PaymentClient5xxException extends PaymentClientException {

    public PaymentClient5xxException() {
        super();
    }

    public PaymentClient5xxException(String message) {
        super(message);
    }

    public PaymentClient5xxException(String message, HttpStatusCode httpStatusCode, int errorCode) {
        super(message, httpStatusCode, errorCode);
    }
}
