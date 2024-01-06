package know_wave.comma.payment.exception;

import org.springframework.http.HttpStatusCode;

public class PaymentClient4xxException extends PaymentClientException {

    public PaymentClient4xxException() {
        super();
    }
    public PaymentClient4xxException(String message) {
        super(message);
    }

    public PaymentClient4xxException(String message, HttpStatusCode httpStatusCode, int errorCode) {
        super(message, httpStatusCode, errorCode);
    }
}
