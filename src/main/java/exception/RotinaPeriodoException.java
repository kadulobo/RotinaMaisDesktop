package exception;

public class RotinaPeriodoException extends RuntimeException {
    public RotinaPeriodoException(String msg) { super(msg); }
    public RotinaPeriodoException(String msg, Throwable t) { super(msg, t); }
}