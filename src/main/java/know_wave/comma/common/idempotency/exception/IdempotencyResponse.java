package know_wave.comma.common.idempotency.exception;

public class IdempotencyResponse extends RuntimeException {

    public IdempotencyResponse(String message) {
        super(message);
    }

}
