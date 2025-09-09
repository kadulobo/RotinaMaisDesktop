// path: src/main/java/exception/IngredienteException.java
package exception;

public class IngredienteException extends RuntimeException {
    public IngredienteException(String message) {
        super(message);
    }

    public IngredienteException(String message, Throwable cause) {
        super(message, cause);
    }
}
