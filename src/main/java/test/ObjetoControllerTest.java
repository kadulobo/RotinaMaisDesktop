package test;

import controller.ObjetoController;
import dao.impl.ObjetoDaoNativeImpl;
import model.Objeto;

import java.util.List;

public class ObjetoControllerTest {
    public static void main(String[] args) {
        ObjetoDaoNativeImpl dao = new ObjetoDaoNativeImpl();
        ObjetoController controller = new ObjetoController(dao);

        Objeto objeto = new Objeto();
        objeto.setNome("Objeto Exemplo");
        controller.criar(objeto);

        List<Objeto> list = controller.listar();
        if (!list.isEmpty()) {
            Objeto buscado = list.get(0);
            buscado.setNome("Objeto Atualizado");
            controller.atualizar(buscado);
            controller.remover(buscado.getIdObjeto());
            controller.criar(objeto);
        }
    }
}
