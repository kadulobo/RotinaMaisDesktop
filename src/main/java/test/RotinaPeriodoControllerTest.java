package test;

import controller.RotinaPeriodoController;
import dao.impl.RotinaPeriodoDaoNativeImpl;
import model.RotinaPeriodo;
import controller.RotinaController;
import controller.PeriodoController;
import dao.impl.RotinaDaoNativeImpl;
import dao.impl.PeriodoDaoNativeImpl;

import java.util.List;

public class RotinaPeriodoControllerTest {
    public static void main(String[] args) {
        RotinaPeriodoDaoNativeImpl dao = new RotinaPeriodoDaoNativeImpl();
        RotinaPeriodoController controller = new RotinaPeriodoController(dao);

        RotinaPeriodo rp = new RotinaPeriodo();
        RotinaController rotinaController = new RotinaController(new RotinaDaoNativeImpl());
        PeriodoController periodoController = new PeriodoController(new PeriodoDaoNativeImpl());
        rp.setIdRotina(TestUtils.getRandom(rotinaController.listar()).getIdRotina());
        rp.setIdPeriodo(TestUtils.getRandom(periodoController.listar()).getIdPeriodo());
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
