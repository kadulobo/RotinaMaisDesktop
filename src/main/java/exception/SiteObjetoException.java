package exception;

public class SiteObjetoException extends RuntimeException {
    public SiteObjetoException(String msg) { super(msg); }
    public SiteObjetoException(String msg, Throwable t) { super(msg, t); }
}