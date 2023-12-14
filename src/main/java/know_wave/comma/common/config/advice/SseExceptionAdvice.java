package know_wave.comma.common.config.advice;


import know_wave.comma.notification.realtime.sse.exception.SseEmitterSendException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(annotations = RestController.class)
public class SseExceptionAdvice {

    @ExceptionHandler(SseEmitterSendException.class)
    public ResponseEntity<String> sseEmitterSendException(SseEmitterSendException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
