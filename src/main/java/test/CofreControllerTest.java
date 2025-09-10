package test;

import controller.CofreController;
import controller.UsuarioController;
import dao.impl.CofreDaoNativeImpl;
import dao.impl.UsuarioDaoNativeImpl;
import model.Cofre;
import model.Usuario;

import java.util.List;

public class CofreControllerTest {
    public static void main(String[] args) {
        CofreDaoNativeImpl dao = new CofreDaoNativeImpl();
        CofreController controller = new CofreController(dao);
        
        UsuarioDaoNativeImpl daoUsu = new UsuarioDaoNativeImpl();
        UsuarioController controllerUsu = new UsuarioController(daoUsu);

        // Recuperar usuário persistido e atualizar
        List<Usuario> usuarios = controllerUsu.listar();
        Usuario buscadoUsu = usuarios.get(0);
        
        Cofre cofre = new Cofre();
        cofre.setLogin("login");
        cofre.setIdUsuario(buscadoUsu.getIdUsuario());
        cofre.setPlataforma("Plataforma x");
        cofre.setSenha("123");
        cofre.setTipo(1);
        controller.criar(cofre);

        List<Cofre> list = controller.listar();
        if (!list.isEmpty()) {
            Cofre buscado = list.get(0);
            controller.atualizar(buscado);
            controller.remover(buscado.getIdCofre());
            controller.criar(cofre);
        }
    }
}
