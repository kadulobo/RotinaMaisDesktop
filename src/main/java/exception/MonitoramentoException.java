// path: src/main/java/exception/MonitoramentoException.java
package exception;

public class MonitoramentoException extends RuntimeException {
    public MonitoramentoException(String message) {
        super(message);
    }

    public MonitoramentoException(String message, Throwable cause) {
        super(message, cause);
    }
}
