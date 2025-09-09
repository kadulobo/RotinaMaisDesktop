package main;

import controller.TreinoController;
import dao.impl.TreinoDaoNativeImpl;
import model.Treino;

import java.util.List;

/**
 * Classe de teste simples para {@link TreinoController}.
 * Utiliza dados fictícios para exercitar as funções do controller.
 */
public class TreinoControllerTest {

    public static void main(String[] args) {
        TreinoDaoNativeImpl dao = new TreinoDaoNativeImpl();
        TreinoController controller = new TreinoController(dao);

        // Criar treino fictício
        Treino treino = new Treino();
        treino.setIdTreino(1);
        treino.setNome("Treino A");
        treino.setClasse("Força");
        treino.setIdRotina(1);
        controller.criar(treino);

        // Atualizar treino
        treino.setNome("Treino B");
        controller.atualizar(treino);

        // Buscar e listar
        Treino buscado = controller.buscarPorId(1);
        List<Treino> treinos = controller.listar();
        controller.listar(0, 10);
        controller.buscarPorNome("Treino");
        controller.buscarPorClasse("Força");
        controller.buscarPorIdRotina(1);
        controller.pesquisar(treino);
        controller.pesquisar(treino, 0, 10);

        System.out.println("Encontrado: " + buscado);
        System.out.println("Total treinos: " + treinos.size());

        // Remover treino
        controller.remover(buscado.getIdTreino());
    }
}
