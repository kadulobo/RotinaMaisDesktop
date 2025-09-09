package test;

import controller.MonitoramentoObjetoController;
import dao.impl.MonitoramentoObjetoDaoNativeImpl;
import model.MonitoramentoObjeto;
import controller.MonitoramentoController;
import controller.ObjetoController;
import dao.impl.MonitoramentoDaoNativeImpl;
import dao.impl.ObjetoDaoNativeImpl;

import java.time.LocalDate;
import java.util.List;

public class MonitoramentoObjetoControllerTest {
    public static void main(String[] args) {
        MonitoramentoObjetoDaoNativeImpl dao = new MonitoramentoObjetoDaoNativeImpl();
        MonitoramentoObjetoController controller = new MonitoramentoObjetoController(dao);

        MonitoramentoObjeto mo = new MonitoramentoObjeto();
        mo.setData(LocalDate.now());
        MonitoramentoController monitoramentoController = new MonitoramentoController(new MonitoramentoDaoNativeImpl());
        ObjetoController objetoController = new ObjetoController(new ObjetoDaoNativeImpl());
        mo.setIdMonitoramento(TestUtils.getRandom(monitoramentoController.listar()).getIdMonitoramento());
        mo.setIdObjeto(TestUtils.getRandom(objetoController.listar()).getIdObjeto());
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
