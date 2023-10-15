package know_wave.comma.common.idempotency.exception;

public class IdempotencyUnprocessableEntity extends RuntimeException {
    public IdempotencyUnprocessableEntity(String message) {
        super(message);
    }
}
