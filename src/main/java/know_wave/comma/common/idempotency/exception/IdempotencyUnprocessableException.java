package know_wave.comma.common.idempotency.exception;

public class IdempotencyUnprocessableException extends RuntimeException {
    public IdempotencyUnprocessableException(String message) {
        super(message);
    }
}
