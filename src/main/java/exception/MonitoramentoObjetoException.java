package exception;

public class MonitoramentoObjetoException extends RuntimeException {
    public MonitoramentoObjetoException(String msg) { super(msg); }
    public MonitoramentoObjetoException(String msg, Throwable t) { super(msg, t); }
}