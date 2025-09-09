package test;

import controller.TreinoExercicioController;
import dao.impl.TreinoExercicioDaoNativeImpl;
import model.TreinoExercicio;
import controller.ExercicioController;
import controller.TreinoController;
import dao.impl.ExercicioDaoNativeImpl;
import dao.impl.TreinoDaoNativeImpl;

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
        ExercicioController exercicioController = new ExercicioController(new ExercicioDaoNativeImpl());
        TreinoController treinoController = new TreinoController(new TreinoDaoNativeImpl());
        te.setIdExercicio(TestUtils.getRandom(exercicioController.listar()).getIdExercicio());
        te.setIdTreino(TestUtils.getRandom(treinoController.listar()).getIdTreino());
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
