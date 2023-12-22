package know_wave.comma.web.advice;

import know_wave.comma.config.security.exception.*;
import know_wave.comma.config.security.exception.SecurityException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice(annotations = RestController.class)
public class SecurityAdvice {

    private static final String MESSAGE = "message";

    @ExceptionHandler({SignInFailureException.class})
    public ResponseEntity<Map<String, String>> handleBadRequestException(SecurityException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(MESSAGE, e.getMessage()));
    }

    @ExceptionHandler({NotFoundTokenException.class, InvalidTokenException.class, TokenExpiredException.class, TokenTemperedException.class})
    public ResponseEntity<Map<String, String>> handleUnAuthenticationException(SecurityException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(MESSAGE, e.getMessage()));
    }

//    @ExceptionHandler()
//    public ResponseEntity<Map<String, String>> handleForbiddenException(SecurityException e) {
//        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of(MESSAGE, e.getMessage()));
//    }

}
