package test;

import controller.MonitoramentoObjetoController;
import dao.impl.MonitoramentoObjetoDaoNativeImpl;
import model.MonitoramentoObjeto;
import controller.MonitoramentoController;
import controller.ObjetoController;
import dao.impl.MonitoramentoDaoNativeImpl;
import dao.impl.ObjetoDaoNativeImpl;
import model.Monitoramento;
import model.Objeto;

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

        Monitoramento monitoramento = TestUtils.getRandom(monitoramentoController.listar());
        Objeto objeto = TestUtils.getRandom(objetoController.listar());
        mo.setIdMonitoramento(monitoramento.getIdMonitoramento());
        mo.setIdObjeto(objeto.getIdObjeto());

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
