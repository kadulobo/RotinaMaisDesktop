package main;

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
        usuario.setIdUsuario(1);
        usuario.setNome("Usuário Exemplo");
        usuario.setSenha("senha123");
        usuario.setEmail("usuario@example.com");
        controller.criar(usuario);

        // Atualizar usuário
        usuario.setNome("Usuário Atualizado");
        controller.atualizar(usuario);

        // Buscar e listar usuários
        Usuario buscado = controller.buscarPorId(1);
        List<Usuario> usuarios = controller.listar();
        System.out.println("Encontrado: " + buscado);
        System.out.println("Total usuarios: " + usuarios.size());

        // Remover usuário
        controller.remover(buscado.getIdUsuario());
    }
}
