package know_wave.comma.payment.exception;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Getter
public class PaymentClientException extends RuntimeException {

    protected HttpStatusCode httpStatusCode;
    protected int errorCode;

    public PaymentClientException() {
        super();
    }

    public PaymentClientException(String message) {
        super(message);
    }

    public PaymentClientException(String message, HttpStatusCode httpStatusCode, int errorCode) {
        super(message);
        this.httpStatusCode = httpStatusCode;
        this.errorCode = errorCode;
    }
}
