package know_wave.comma.web.advice;

import know_wave.comma.account.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.rmi.AlreadyBoundException;
import java.util.Map;

@RestControllerAdvice(annotations = RestController.class)
public class AccountAdvice {

    private static final String MESSAGE = "message";

    @ExceptionHandler({AccountStatusException.class, AccountAuthorityException.class})
    public ResponseEntity<Map<String, String>> handleForbiddenException(AccountException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of(MESSAGE, e.getMessage()));
    }

    @ExceptionHandler({NotFoundEmailException.class, AlreadyBoundException.class})
    public ResponseEntity<Map<String, String>> handleBadRequestException(AccountException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(MESSAGE, e.getMessage()));
    }

    @ExceptionHandler(NotVerifiedException.class)
    public ResponseEntity<Map<String, String>> handleUnAuthenticationException(AccountException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(MESSAGE, e.getMessage()));
    }
}
