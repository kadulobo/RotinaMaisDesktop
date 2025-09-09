package exception;

public class CarteiraException extends RuntimeException {
    public CarteiraException(String msg) { super(msg); }
    public CarteiraException(String msg, Throwable t) { super(msg, t); }
}