package test;

import controller.RotinaPeriodoController;
import dao.impl.RotinaPeriodoDaoNativeImpl;
import model.RotinaPeriodo;

import java.util.List;

public class RotinaPeriodoControllerTest {
    public static void main(String[] args) {
        RotinaPeriodoDaoNativeImpl dao = new RotinaPeriodoDaoNativeImpl();
        RotinaPeriodoController controller = new RotinaPeriodoController(dao);

        RotinaPeriodo rp = new RotinaPeriodo();
        rp.setIdRotina(1);
        rp.setIdPeriodo(1);
        controller.criar(rp);

        List<RotinaPeriodo> list = controller.listar();
        if (!list.isEmpty()) {
            RotinaPeriodo buscado = list.get(0);
            controller.atualizar(buscado);
            controller.remover(buscado.getIdRotinaPeriodo());
            controller.criar(rp);
        }
    }
}
