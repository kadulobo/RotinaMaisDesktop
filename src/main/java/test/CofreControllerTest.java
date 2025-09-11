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
        cofre.setFoto(new byte[]{1});
        controller.criar(cofre);
        

        List<Cofre> list = controller.listar();
        if (!list.isEmpty()) {
            Cofre buscado = list.get(0);
            controller.atualizar(buscado);
            controller.remover(buscado.getIdCofre());
            controller.criar(cofre);
        }
        for (int i = 0; i < 100; i++) {
              cofre.setLogin("login"+i);
              cofre.setIdUsuario(buscadoUsu.getIdUsuario());
              cofre.setPlataforma("Plataforma x"+i);
              cofre.setSenha("123"+i);
              cofre.setTipo(1);
              cofre.setFoto(new byte[]{1});
              controller.criar(cofre);      
		}
        for (int i = 100; i < 200; i++) {
            cofre.setLogin("login"+i);
            cofre.setIdUsuario(buscadoUsu.getIdUsuario());
            cofre.setPlataforma("Plataforma x"+i);
            cofre.setSenha("123"+i);
            cofre.setTipo(2);
            cofre.setFoto(new byte[]{1});
            controller.criar(cofre);      
		}
        for (int i = 200; i < 300; i++) {
            cofre.setLogin("login"+i);
            cofre.setIdUsuario(buscadoUsu.getIdUsuario());
            cofre.setPlataforma("Plataforma x"+i);
            cofre.setSenha("123"+i);
            cofre.setTipo(3);
            cofre.setFoto(new byte[]{1});
            controller.criar(cofre);      
		}
    }
}
