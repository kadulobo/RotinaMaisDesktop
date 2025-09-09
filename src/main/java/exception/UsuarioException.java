// path: src/main/java/exception/UsuarioException.java
package exception;

public class UsuarioException extends RuntimeException {
    public UsuarioException() {
        super();
    }

    public UsuarioException(String message) {
        super(message);
    }

    public UsuarioException(String message, Throwable cause) {
        super(message, cause);
    }

    public UsuarioException(Throwable cause) {
        super(cause);
    }
}
