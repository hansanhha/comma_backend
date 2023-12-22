package know_wave.comma.config.security.exception;

public class InvalidTokenException extends SecurityException {

    public InvalidTokenException() {
        super();
    }

    public InvalidTokenException(String message) {
        super(message);
    }
}
