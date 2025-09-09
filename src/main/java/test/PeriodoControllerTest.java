package test;

import controller.PeriodoController;
import dao.impl.PeriodoDaoNativeImpl;
import model.Periodo;

import java.util.List;

public class PeriodoControllerTest {
    public static void main(String[] args) {
        PeriodoDaoNativeImpl dao = new PeriodoDaoNativeImpl();
        PeriodoController controller = new PeriodoController(dao);

        Periodo periodo = new Periodo();
        periodo.setAno(2024);
        periodo.setMes(1);
        controller.criar(periodo);

        List<Periodo> list = controller.listar();
        if (!list.isEmpty()) {
            Periodo buscado = list.get(0);
            controller.atualizar(buscado);
            controller.remover(buscado.getIdPeriodo());
            controller.criar(periodo);
        }
    }
}
