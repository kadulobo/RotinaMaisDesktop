// path: src/main/java/exception/ExercicioException.java
package exception;

public class ExercicioException extends RuntimeException {

    public ExercicioException(String message) {
        super(message);
    }

    public ExercicioException(String message, Throwable cause) {
        super(message, cause);
    }
}
