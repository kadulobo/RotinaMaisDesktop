package main;

import controller.PapelController;
import dao.impl.PapelDaoNativeImpl;
import model.Papel;

import java.time.LocalDate;
import java.util.List;

public class PapelControllerTest {
    public static void main(String[] args) {
        PapelDaoNativeImpl dao = new PapelDaoNativeImpl();
        PapelController controller = new PapelController(dao);

        Papel papel = new Papel();
        papel.setCodigo("COD");
        papel.setTipo("TIPO");
        papel.setVencimento(LocalDate.now());
        controller.criar(papel);

        List<Papel> list = controller.listar();
        if (!list.isEmpty()) {
            Papel buscado = list.get(0);
            controller.atualizar(buscado);
            controller.remover(buscado.getIdPapel());
            controller.criar(papel);
        }
    }
}
