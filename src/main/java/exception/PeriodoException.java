// path: src/main/java/exception/PeriodoException.java
package exception;

public class PeriodoException extends RuntimeException {
    public PeriodoException(String message) {
        super(message);
    }

    public PeriodoException(String message, Throwable cause) {
        super(message, cause);
    }
}
