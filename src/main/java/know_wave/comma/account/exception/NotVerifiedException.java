package know_wave.comma.account.exception;

public class NotVerifiedException extends AccountException {

    public NotVerifiedException() {
        super();
    }
    public NotVerifiedException(String notVerifiedEmail) {
        super(notVerifiedEmail);
    }
}
