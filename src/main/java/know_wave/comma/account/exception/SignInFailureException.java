package know_wave.comma.account.exception;

import org.springframework.security.core.AuthenticationException;

public class SignInFailureException extends AuthenticationException {
    public SignInFailureException(String msg) {
        super(msg);
    }
}
