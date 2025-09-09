package test;

import controller.UsuarioController;
import dao.impl.UsuarioDaoNativeImpl;
import model.Usuario;

import java.util.List;

/**
 * Classe de teste simples para {@link UsuarioController}.
 * Utiliza dados fictícios para exercitar as operações básicas
 * de criação, atualização, listagem e remoção de usuários.
 */
public class UsuarioControllerTest {

    public static void main(String[] args) {
        UsuarioDaoNativeImpl dao = new UsuarioDaoNativeImpl();
        UsuarioController controller = new UsuarioController(dao);

        // Criar usuário fictício
        Usuario usuario = new Usuario();
        usuario.setNome("Usuário Exemplo");
        usuario.setSenha("senha123");
        usuario.setEmail("usuario@example.com");
        usuario.setCpf("12345678900");
        controller.criar(usuario);

        // Recuperar usuário persistido e atualizar
        List<Usuario> usuarios = controller.listar();
        Usuario buscado = controller.buscarPorCpf("12345678900").get(0);
        buscado.setNome("Usuário Atualizado");
        controller.atualizar(buscado);

        // Listar usuários
        usuarios = controller.listar();
        System.out.println("Encontrado: " + buscado);
        System.out.println("Total usuarios: " + usuarios.size());

        // Remover usuário
        controller.remover(buscado.getIdUsuario());

        // Recriar usuário para manter dados na tabela
        controller.criar(usuario);
    }
}
