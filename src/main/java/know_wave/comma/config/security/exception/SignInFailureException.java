package know_wave.comma.config.security.exception;

import org.springframework.security.core.AuthenticationException;

public class SignInFailureException extends AuthenticationException {
    public SignInFailureException(String msg) {
        super(msg);
    }
}
