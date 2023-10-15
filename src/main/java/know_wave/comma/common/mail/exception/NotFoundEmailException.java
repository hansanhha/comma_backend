package know_wave.comma.common.mail.exception;

import jakarta.persistence.EntityNotFoundException;

public class NotFoundEmailException extends EntityNotFoundException {
    public NotFoundEmailException() {
        super();
    }

    public NotFoundEmailException(Exception cause) {
        super(cause);
    }

    public NotFoundEmailException(String message) {
        super(message);
    }

    public NotFoundEmailException(String message, Exception cause) {
        super(message, cause);
    }
}
