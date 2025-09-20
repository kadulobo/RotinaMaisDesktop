package test;

import controller.CadernoController;
import controller.CategoriaController;
import controller.UsuarioController;
import dao.impl.CadernoDaoNativeImpl;
import dao.impl.CategoriaDaoNativeImpl;
import dao.impl.UsuarioDaoNativeImpl;
import model.Caderno;
import model.Categoria;
import model.Usuario;

import java.time.LocalDate;
import java.util.List;

public class CadernoControllerTest {
    public static void main(String[] args) {
        CadernoController controller = new CadernoController(new CadernoDaoNativeImpl());
        UsuarioController usuarioController = new UsuarioController(new UsuarioDaoNativeImpl());
        CategoriaController categoriaController = new CategoriaController(new CategoriaDaoNativeImpl());

        List<Usuario> usuarios = usuarioController.listar();
        List<Categoria> categorias = categoriaController.listar();
        if (usuarios.isEmpty() || categorias.isEmpty()) {
            System.out.println("Necessário ter usuário e categoria cadastrados para o teste de Caderno");
            return;
        }
        Usuario usuario = usuarios.get(0);
        Categoria categoria = categorias.get(0);

        Caderno caderno = new Caderno();
        caderno.setTitulo("Resumo Diario");
        caderno.setComando("Gerar resumo");
        caderno.setData(LocalDate.now());
        caderno.setNomeIa("Assistente");
        caderno.setObjetivo("Organizar tarefas");
        caderno.setResultado("Resumo gerado");
        caderno.setIdUsuario(usuario.getIdUsuario());
        caderno.setIdCategoria(categoria.getIdCategoria());
        controller.criar(caderno);

        List<Caderno> list = controller.listar();
        if (!list.isEmpty()) {
            Caderno buscado = list.get(0);
            buscado.setResultado("Atualizado");
            controller.atualizar(buscado);
            controller.remover(buscado.getIdCaderno());
        }
    }
}
