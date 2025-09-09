package exception;

public class PapelException extends RuntimeException {
    public PapelException(String msg) { super(msg); }
    public PapelException(String msg, Throwable t) { super(msg, t); }
}