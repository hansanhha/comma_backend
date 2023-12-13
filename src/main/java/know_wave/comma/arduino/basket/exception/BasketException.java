package know_wave.comma.arduino.basket.exception;

public class BasketException extends RuntimeException {

    public BasketException(String message) {
        super(message);
    }

    public BasketException(String message, Throwable cause) {
        super(message, cause);
    }
}
