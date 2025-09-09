package test;

import controller.LancamentoController;
import dao.impl.LancamentoDaoNativeImpl;
import model.Lancamento;
import controller.EventoController;
import dao.impl.EventoDaoNativeImpl;

import java.math.BigDecimal;
import java.util.List;
import java.time.LocalDate;

public class LancamentoControllerTest {
    public static void main(String[] args) {
        LancamentoDaoNativeImpl dao = new LancamentoDaoNativeImpl();
        LancamentoController controller = new LancamentoController(dao);

        Lancamento lancamento = new Lancamento();
        lancamento.setValor(BigDecimal.ONE);
        EventoController eventoController = new EventoController(new EventoDaoNativeImpl());
        lancamento.setEvento(TestUtils.getRandom(eventoController.listar()));
        lancamento.setFixo(Boolean.FALSE);
        lancamento.setDataPagamento(LocalDate.now());
        controller.criar(lancamento);

        List<Lancamento> list = controller.listar();
        if (!list.isEmpty()) {
            Lancamento buscado = list.get(0);
            controller.atualizar(buscado);
            controller.remover(buscado.getIdLancamento());
            controller.criar(lancamento);
        }
    }
}
