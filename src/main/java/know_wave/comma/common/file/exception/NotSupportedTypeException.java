package know_wave.comma.common.file.exception;

public class NotSupportedTypeException extends RuntimeException{

    public NotSupportedTypeException(String message) {
        super(message);
    }

    public NotSupportedTypeException(String message, Throwable cause) {
        super(message, cause);
    }
}
