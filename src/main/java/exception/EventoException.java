// path: src/main/java/exception/EventoException.java
package exception;

public class EventoException extends RuntimeException {
    public EventoException(String message) {
        super(message);
    }

    public EventoException(String message, Throwable cause) {
        super(message, cause);
    }
}
