package main;

import controller.MonitoramentoObjetoController;
import dao.impl.MonitoramentoObjetoDaoNativeImpl;
import model.MonitoramentoObjeto;

import java.time.LocalDate;
import java.util.List;

public class MonitoramentoObjetoControllerTest {
    public static void main(String[] args) {
        MonitoramentoObjetoDaoNativeImpl dao = new MonitoramentoObjetoDaoNativeImpl();
        MonitoramentoObjetoController controller = new MonitoramentoObjetoController(dao);

        MonitoramentoObjeto mo = new MonitoramentoObjeto();
        mo.setData(LocalDate.now());
        mo.setIdMonitoramento(1);
        mo.setIdObjeto(1);
        controller.criar(mo);

        List<MonitoramentoObjeto> list = controller.listar();
        if (!list.isEmpty()) {
            MonitoramentoObjeto buscado = list.get(0);
            controller.atualizar(buscado);
            controller.remover(buscado.getIdMonitoramentoObjeto());
            controller.criar(mo);
        }
    }
}
