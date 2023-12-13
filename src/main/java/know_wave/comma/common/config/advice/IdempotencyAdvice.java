package know_wave.comma.common.config.advice;

import know_wave.comma.common.idempotency.exception.IdempotencyResponse;
import know_wave.comma.common.idempotency.exception.IdempotencyUnprocessableException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(annotations = RestController.class)
public class IdempotencyAdvice {

    @ExceptionHandler(IdempotencyResponse.class)
    public ResponseEntity<String> idempotency(IdempotencyResponse ex) {
        String[] messages = ex.getMessage().split(", idempotentResponse: ");

        int status = Integer.parseInt(messages[0]);
        String responseMessage = messages[1];

        return new ResponseEntity<>(responseMessage, HttpStatus.valueOf(status));
    }

    @ExceptionHandler(IdempotencyUnprocessableException.class)
    public ResponseEntity<String> idempotencyUnprocessableEntity(IdempotencyUnprocessableException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
