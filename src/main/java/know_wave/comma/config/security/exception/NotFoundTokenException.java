package know_wave.comma.config.security.exception;

public class NotFoundTokenException extends SecurityException {

    public NotFoundTokenException() {
        super();
    }

    public NotFoundTokenException(String message) {
        super(message);
    }
}
