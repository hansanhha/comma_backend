package know_wave.comma.account.exception;

public class AlreadyVerifiedException extends RuntimeException{

    public AlreadyVerifiedException() {
        super();
    }

    public AlreadyVerifiedException(String message) {
        super(message);
    }
}
