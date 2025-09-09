package main;

import controller.CofreController;
import dao.impl.CofreDaoNativeImpl;
import model.Cofre;

import java.util.List;

public class CofreControllerTest {
    public static void main(String[] args) {
        CofreDaoNativeImpl dao = new CofreDaoNativeImpl();
        CofreController controller = new CofreController(dao);

        Cofre cofre = new Cofre();
        cofre.setLogin("login");
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
