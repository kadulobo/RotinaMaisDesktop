package exception;

public class TreinoExercicioException extends RuntimeException {
    public TreinoExercicioException(String msg) { super(msg); }
    public TreinoExercicioException(String msg, Throwable t) { super(msg, t); }
}