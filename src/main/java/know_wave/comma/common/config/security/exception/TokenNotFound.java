package know_wave.comma.common.config.security.exception;

public class TokenNotFound extends RuntimeException{

    public TokenNotFound() {
    }

    public TokenNotFound(String message) {
        super(message);
    }
}
