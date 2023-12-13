package know_wave.comma.arduino.order.admin.exception;

public class AdminOrderException extends RuntimeException {

        public AdminOrderException(String message) {
            super(message);
        }

        public AdminOrderException(String message, Throwable cause) {
            super(message, cause);
        }
}
