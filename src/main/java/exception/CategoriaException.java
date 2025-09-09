// path: src/main/java/exception/CategoriaException.java
package exception;

public class CategoriaException extends RuntimeException {
    public CategoriaException(String message) {
        super(message);
    }

    public CategoriaException(String message, Throwable cause) {
        super(message, cause);
    }
}
