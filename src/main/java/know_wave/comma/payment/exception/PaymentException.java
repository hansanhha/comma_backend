package know_wave.comma.payment.exception;

public class PaymentException extends RuntimeException {

    public PaymentException() {
        super();
    }

    public PaymentException(String message) {
        super(message);
    }
}
