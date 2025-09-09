// path: src/main/java/exception/LancamentoException.java
package exception;

public class LancamentoException extends RuntimeException {

    public LancamentoException(String message) {
        super(message);
    }

    public LancamentoException(String message, Throwable cause) {
        super(message, cause);
    }
}
