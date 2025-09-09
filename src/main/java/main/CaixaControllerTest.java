package main;

import controller.CaixaController;
import dao.impl.CaixaDaoNativeImpl;
import model.Caixa;

import java.util.List;
import java.math.BigDecimal;

public class CaixaControllerTest {
    public static void main(String[] args) {
        CaixaDaoNativeImpl dao = new CaixaDaoNativeImpl();
        CaixaController controller = new CaixaController(dao);

        Caixa caixa = new Caixa();
        caixa.setNome("Caixa Exemplo");
        caixa.setReservaEmergencia(BigDecimal.ZERO);
        caixa.setSalarioMedio(BigDecimal.ZERO);
        caixa.setValorTotal(BigDecimal.ZERO);
        controller.criar(caixa);

        List<Caixa> list = controller.listar();
        if (!list.isEmpty()) {
            Caixa buscado = list.get(0);
            buscado.setNome("Caixa Atualizada");
            controller.atualizar(buscado);
            controller.remover(buscado.getIdCaixa());
            controller.criar(caixa);
        }
    }
}
