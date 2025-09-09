package main;

import controller.MetaController;
import dao.impl.MetaDaoNativeImpl;
import model.Meta;

import java.util.List;

public class MetaControllerTest {
    public static void main(String[] args) {
        MetaDaoNativeImpl dao = new MetaDaoNativeImpl();
        MetaController controller = new MetaController(dao);

        Meta meta = new Meta();
        controller.criar(meta);

        List<Meta> list = controller.listar();
        if (!list.isEmpty()) {
            Meta buscado = list.get(0);
            controller.atualizar(buscado);
            controller.remover(buscado.getIdMeta());
            controller.criar(meta);
        }
    }
}
