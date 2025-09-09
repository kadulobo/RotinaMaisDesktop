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
        treino.setNome("Treino A");
        treino.setClasse("Força");
        treino.setIdRotina(1);
        controller.criar(treino);

        // Recuperar treino persistido e atualizar
        List<Treino> treinos = controller.listar();
        Treino buscado = controller.buscarPorId(treinos.get(0).getIdTreino());
        buscado.setNome("Treino B");
        controller.atualizar(buscado);

        // Buscar e listar
        treinos = controller.listar();
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

        // Recriar treino para manter dados na tabela
        controller.criar(treino);
    }
}
