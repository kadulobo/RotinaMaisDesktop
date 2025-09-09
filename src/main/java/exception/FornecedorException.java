// path: src/main/java/exception/FornecedorException.java
package exception;

public class FornecedorException extends RuntimeException {
    public FornecedorException(String message) {
        super(message);
    }

    public FornecedorException(String message, Throwable cause) {
        super(message, cause);
    }
}
