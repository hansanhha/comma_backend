package know_wave.comma.payment.exception;

public class PaymentClientException extends RuntimeException {

    public PaymentClientException() {
        super();
    }

    public PaymentClientException(String message) {
        super(message);
    }
}
