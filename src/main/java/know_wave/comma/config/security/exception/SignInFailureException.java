package know_wave.comma.config.security.exception;

import org.springframework.security.core.AuthenticationException;

public class SignInFailureException extends SecurityException {

public SignInFailureException() {
        super();
    }

    public SignInFailureException(String msg) {
        super(msg);
    }
}
