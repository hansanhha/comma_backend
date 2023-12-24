package know_wave.comma.account.exception;

import jakarta.persistence.EntityNotFoundException;

public class NotFoundEmailException extends AccountException {

    public NotFoundEmailException() {
        super();
    }
    public NotFoundEmailException(String message) {
        super(message);
    }

}
