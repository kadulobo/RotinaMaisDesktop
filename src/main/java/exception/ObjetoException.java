// path: src/main/java/exception/ObjetoException.java
package exception;

public class ObjetoException extends RuntimeException {
    public ObjetoException(String message) {
        super(message);
    }

    public ObjetoException(String message, Throwable cause) {
        super(message, cause);
    }
}
