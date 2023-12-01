package know_wave.comma.alarm.exception;

public class EmailVerifiedException extends RuntimeException{

    public EmailVerifiedException() {
        super();
    }

    public EmailVerifiedException(String message) {
        super(message);
    }
}
