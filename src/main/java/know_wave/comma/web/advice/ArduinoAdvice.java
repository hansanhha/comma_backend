package know_wave.comma.web.advice;

import know_wave.comma.arduino.basket.exception.BasketException;
import know_wave.comma.arduino.component.admin.exception.AlreadyCategoryException;
import know_wave.comma.arduino.order.exception.OrderException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice(annotations = RestController.class)
public class ArduinoAdvice {

    private static final String MESSAGE = "message";

    @ExceptionHandler(BasketException.class)
    public ResponseEntity<Map<String, String>> orderException(BasketException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(MESSAGE, e.getMessage()));
    }

    @ExceptionHandler(OrderException.class)
    public ResponseEntity<Map<String, String>> orderException(OrderException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(MESSAGE, e.getMessage()));
    }

    @ExceptionHandler(AlreadyCategoryException.class)
    public ResponseEntity<Map<String, String>> alreadyCategoryException(AlreadyCategoryException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(MESSAGE, e.getMessage()));
    }
}
