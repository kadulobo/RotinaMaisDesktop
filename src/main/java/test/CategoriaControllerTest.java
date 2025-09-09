package test;

import controller.CategoriaController;
import dao.impl.CategoriaDaoNativeImpl;
import model.Categoria;

import java.time.LocalDate;
import java.util.List;

/**
 * Classe de teste simples para {@link CategoriaController}.
 * Utiliza dados fictícios para exercitar as operações básicas
 * e consultas diversas com o tema da aplicação.
 */
public class CategoriaControllerTest {

    public static void main(String[] args) {
        CategoriaDaoNativeImpl dao = new CategoriaDaoNativeImpl();
        CategoriaController controller = new CategoriaController(dao);

        // Criar categoria fictícia
        Categoria categoria = new Categoria();
        categoria.setIdCategoria(1);
        categoria.setNome("Saúde");
        categoria.setDescricao("Itens de saúde");
        categoria.setDataCriacao(LocalDate.now());
        controller.criar(categoria);

        // Atualizar categoria
        categoria.setNome("Saúde e Bem Estar");
        controller.atualizar(categoria);

        // Buscar e listar
        Categoria buscado = controller.buscarPorId(1);
        List<Categoria> categorias = controller.listar();
        controller.listar(0, 10);
        controller.buscarPorNome("Saúde");
        controller.buscarPorDescricao("Itens");
        controller.buscarPorDataCriacao(LocalDate.now());
        controller.pesquisar(categoria);
        controller.pesquisar(categoria, 0, 10);

        System.out.println("Encontrada: " + buscado);
        System.out.println("Total categorias: " + categorias.size());

        // Remover categoria
        controller.remover(buscado.getIdCategoria());

        // Recriar categoria para manter dados na tabela
        controller.criar(categoria);
    }
}
