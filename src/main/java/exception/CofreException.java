package exception;

public class CofreException extends RuntimeException {
    public CofreException(String message) { super(message); }
    public CofreException(String message, Throwable cause) { super(message, cause); }
}
