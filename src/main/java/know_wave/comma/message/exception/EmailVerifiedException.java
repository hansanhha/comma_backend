package know_wave.comma.message.exception;

public class EmailVerifiedException extends RuntimeException{

    public EmailVerifiedException() {
        super();
    }

    public EmailVerifiedException(String message) {
        super(message);
    }
}
