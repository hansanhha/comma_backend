package know_wave.comma.config.security.exception;

public class TokenExpiredException extends SecurityException {

    public TokenExpiredException() {
        super();
    }

    public TokenExpiredException(String message) {
        super(message);
    }
}
