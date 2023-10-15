package know_wave.comma.payment.exception;

public class AlreadyRefundedException extends RuntimeException {
    public AlreadyRefundedException(String message) {
        super(message);
    }
}
