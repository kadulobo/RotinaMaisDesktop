// path: src/main/java/exception/SiteException.java
package exception;

public class SiteException extends RuntimeException {
    public SiteException(String message) {
        super(message);
    }

    public SiteException(String message, Throwable cause) {
        super(message, cause);
    }
}
