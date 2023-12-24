package know_wave.comma.web.advice;

import know_wave.comma.arduino.order.exception.OrderException;
import know_wave.comma.arduino.order.exception.UnableOrderUpdateStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice(annotations = RestController.class)
public class ArduinoOrderAdvice {

    private static final String MESSAGE = "msg";

    @ExceptionHandler({OrderException.class, UnableOrderUpdateStatus.class})
    public ResponseEntity<Map<String, String>> handleBadRequestException(OrderException e) {
        return ResponseEntity.badRequest().body(Map.of(MESSAGE, e.getMessage()));
    }

}
