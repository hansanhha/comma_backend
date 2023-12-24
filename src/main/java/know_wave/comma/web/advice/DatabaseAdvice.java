package know_wave.comma.web.advice;

import org.springframework.dao.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(annotations = RestController.class)
public class DatabaseAdvice {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> pkOrUniqueDuplicateException(DataIntegrityViolationException ex) {
        return new ResponseEntity<>("잘못된 값에 의해 거절되었습니다", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({QueryTimeoutException.class, ConcurrencyFailureException.class, CannotAcquireLockException.class})
    public ResponseEntity<String> queryTimeException(TransientDataAccessException ex) {
        return new ResponseEntity<>("일시적인 오류가 발생했습니다. 잠시 후 다시 시도해주세요", HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
