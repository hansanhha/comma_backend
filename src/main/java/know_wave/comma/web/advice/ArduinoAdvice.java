package know_wave.comma.web.advice;

import know_wave.comma.arduino.cart.exception.CartException;
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

    @ExceptionHandler({CartException.class, AlreadyCategoryException.class})
    public ResponseEntity<Map<String, String>> handleBadRequestException(CartException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(MESSAGE, e.getMessage()));
    }

}
