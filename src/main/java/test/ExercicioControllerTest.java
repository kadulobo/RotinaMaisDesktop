package test;

import controller.ExercicioController;
import dao.impl.ExercicioDaoNativeImpl;
import model.Exercicio;

import java.util.List;

public class ExercicioControllerTest {
    public static void main(String[] args) {
        ExercicioDaoNativeImpl dao = new ExercicioDaoNativeImpl();
        ExercicioController controller = new ExercicioController(dao);

        Exercicio exercicio = new Exercicio();
        exercicio.setNome("Exercicio Exemplo");
        controller.criar(exercicio);

        List<Exercicio> list = controller.listar();
        if (!list.isEmpty()) {
            Exercicio buscado = list.get(0);
            buscado.setNome("Exercicio Atualizado");
            controller.atualizar(buscado);
            controller.remover(buscado.getIdExercicio());
            controller.criar(exercicio);
        }
    }
}
