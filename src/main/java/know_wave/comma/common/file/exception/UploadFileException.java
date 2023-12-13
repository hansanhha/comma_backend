package know_wave.comma.common.file.exception;

public class UploadFileException extends RuntimeException{

    public UploadFileException(String message) {
        super(message);
    }

    public UploadFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
