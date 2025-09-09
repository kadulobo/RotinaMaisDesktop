package test;

import controller.MonitoramentoController;
import dao.impl.MonitoramentoDaoNativeImpl;
import model.Monitoramento;

import java.util.List;

public class MonitoramentoControllerTest {
    public static void main(String[] args) {
        MonitoramentoDaoNativeImpl dao = new MonitoramentoDaoNativeImpl();
        MonitoramentoController controller = new MonitoramentoController(dao);

        Monitoramento monitoramento = new Monitoramento();
        monitoramento.setNome("Monitoramento Exemplo");
        controller.criar(monitoramento);

        List<Monitoramento> list = controller.listar();
        if (!list.isEmpty()) {
            Monitoramento buscado = list.get(0);
            buscado.setNome("Monitoramento Atualizado");
            controller.atualizar(buscado);
            controller.remover(buscado.getIdMonitoramento());
            controller.criar(monitoramento);
        }
    }
}
