package test;

import controller.TreinoExercicioController;
import dao.impl.TreinoExercicioDaoNativeImpl;
import model.TreinoExercicio;

import java.util.List;

public class TreinoExercicioControllerTest {
    public static void main(String[] args) {
        TreinoExercicioDaoNativeImpl dao = new TreinoExercicioDaoNativeImpl();
        TreinoExercicioController controller = new TreinoExercicioController(dao);

        TreinoExercicio te = new TreinoExercicio();
        te.setQtdRepeticao(1);
        te.setTempoDescanso("1m");
        te.setOrdem(1);
        te.setFeito(Boolean.FALSE);
        te.setIdExercicio(1);
        te.setIdTreino(1);
        controller.criar(te);

        List<TreinoExercicio> list = controller.listar();
        if (!list.isEmpty()) {
            TreinoExercicio buscado = list.get(0);
            controller.atualizar(buscado);
            controller.remover(buscado.getIdTreinoExercicio());
            controller.criar(te);
        }
    }
}
