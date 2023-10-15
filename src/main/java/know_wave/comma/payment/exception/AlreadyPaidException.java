package know_wave.comma.payment.exception;

public class AlreadyPaidException extends RuntimeException {

    public AlreadyPaidException(String message) {
        super(message);
    }
}
