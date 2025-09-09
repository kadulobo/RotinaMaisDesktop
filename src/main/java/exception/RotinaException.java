// path: src/main/java/exception/RotinaException.java
package exception;

public class RotinaException extends RuntimeException {
    public RotinaException(String message) {
        super(message);
    }

    public RotinaException(String message, Throwable cause) {
        super(message, cause);
    }
}
