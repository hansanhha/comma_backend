package know_wave.comma.account.exception;

import jakarta.persistence.EntityExistsException;

public class EntityAlreadyExistException extends EntityExistsException {
    public EntityAlreadyExistException() {
    }

    public EntityAlreadyExistException(String message) {
        super(message);
    }
}
