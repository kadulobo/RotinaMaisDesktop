package main;

import controller.LancamentoController;
import dao.impl.LancamentoDaoNativeImpl;
import model.Lancamento;
import model.Evento;

import java.math.BigDecimal;
import java.util.List;
import java.time.LocalDate;

public class LancamentoControllerTest {
    public static void main(String[] args) {
        LancamentoDaoNativeImpl dao = new LancamentoDaoNativeImpl();
        LancamentoController controller = new LancamentoController(dao);

        Lancamento lancamento = new Lancamento();
        lancamento.setValor(BigDecimal.ONE);
        Evento evento = new Evento();
        evento.setIdEvento(1);
        lancamento.setEvento(evento);
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
