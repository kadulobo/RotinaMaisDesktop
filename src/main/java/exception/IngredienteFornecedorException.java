package exception;

public class IngredienteFornecedorException extends RuntimeException {
    public IngredienteFornecedorException(String msg) { super(msg); }
    public IngredienteFornecedorException(String msg, Throwable t) { super(msg, t); }
}