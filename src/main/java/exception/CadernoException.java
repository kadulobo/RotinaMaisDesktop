package exception;

public class CadernoException extends RuntimeException {
    public CadernoException(String message) {
        super(message);
    }

    public CadernoException(String message, Throwable cause) {
        super(message, cause);
    }
}
