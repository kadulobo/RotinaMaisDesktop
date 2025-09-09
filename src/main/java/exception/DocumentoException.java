// path: src/main/java/exception/DocumentoException.java
package exception;

public class DocumentoException extends RuntimeException {

    public DocumentoException(String message) {
        super(message);
    }

    public DocumentoException(String message, Throwable cause) {
        super(message, cause);
    }
}
