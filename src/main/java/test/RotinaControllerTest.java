package test;

import controller.RotinaController;
import dao.impl.RotinaDaoNativeImpl;
import model.Rotina;

import java.time.LocalDate;
import java.util.List;

public class RotinaControllerTest {
    public static void main(String[] args) {
        RotinaDaoNativeImpl dao = new RotinaDaoNativeImpl();
        RotinaController controller = new RotinaController(dao);

        Rotina rotina = new Rotina();
        rotina.setNome("Rotina Exemplo");
        rotina.setInicio(LocalDate.now());
        rotina.setFim(LocalDate.now());
        controller.criar(rotina);

        List<Rotina> list = controller.listar();
        if (!list.isEmpty()) {
            Rotina buscado = list.get(0);
            buscado.setNome("Rotina Atualizada");
            controller.atualizar(buscado);
            controller.remover(buscado.getIdRotina());
            controller.criar(rotina);
        }
    }
}
