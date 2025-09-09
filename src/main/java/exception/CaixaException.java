// path: src/main/java/exception/CaixaException.java
package exception;

public class CaixaException extends RuntimeException {
    public CaixaException(String message) {
        super(message);
    }

    public CaixaException(String message, Throwable cause) {
        super(message, cause);
    }
}
