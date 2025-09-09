package exception;

public class AlimentacaoIngredienteException extends RuntimeException {
    public AlimentacaoIngredienteException(String msg) { super(msg); }
    public AlimentacaoIngredienteException(String msg, Throwable t) { super(msg, t); }
}