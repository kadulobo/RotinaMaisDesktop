package exception;

public class OperacaoException extends RuntimeException {
    public OperacaoException(String msg) { super(msg); }
    public OperacaoException(String msg, Throwable t) { super(msg, t); }
}