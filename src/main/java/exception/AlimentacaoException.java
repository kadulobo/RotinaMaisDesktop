// path: src/main/java/exception/AlimentacaoException.java
package exception;

public class AlimentacaoException extends RuntimeException {
    public AlimentacaoException(String message) {
        super(message);
    }

    public AlimentacaoException(String message, Throwable cause) {
        super(message, cause);
    }
}
