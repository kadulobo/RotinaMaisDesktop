// path: src/main/java/exception/TreinoException.java
package exception;

public class TreinoException extends RuntimeException {
    public TreinoException(String message) {
        super(message);
    }

    public TreinoException(String message, Throwable cause) {
        super(message, cause);
    }
}
