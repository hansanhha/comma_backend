package know_wave.comma.common.config.advice;

import know_wave.comma.common.idempotency.exception.IdempotencyUnprocessableException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(annotations = RestController.class)
public class IdempotencyAdvice {

    @ExceptionHandler(IdempotencyUnprocessableException.class)
    public ResponseEntity<String> idempotencyUnprocessableEntity(IdempotencyUnprocessableException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
