package main;

import controller.OperacaoController;
import dao.impl.OperacaoDaoNativeImpl;
import model.Operacao;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;

public class OperacaoControllerTest {
    public static void main(String[] args) {
        OperacaoDaoNativeImpl dao = new OperacaoDaoNativeImpl();
        OperacaoController controller = new OperacaoController(dao);

        Operacao op = new Operacao();
        op.setFechamento(BigDecimal.ONE);
        op.setTempoOperacao(LocalTime.now());
        op.setQtdCompra(1);
        op.setAbertura(BigDecimal.ONE);
        op.setQtdVenda(1);
        op.setLado("L");
        op.setPrecoCompra(BigDecimal.ONE);
        op.setPrecoVenda(BigDecimal.ONE);
        op.setPrecoMedio(BigDecimal.ONE);
        op.setResIntervalo("res");
        op.setNumeroOperacao(BigDecimal.ONE);
        op.setResOperacao("res");
        op.setDrawdon(BigDecimal.ONE);
        op.setGanhoMax(BigDecimal.ONE);
        op.setPerdaMax(BigDecimal.ONE);
        op.setTet("tet");
        op.setTotal(BigDecimal.ONE);
        op.setIdCarteira(1);
        op.setIdPapel(1);
        controller.criar(op);

        List<Operacao> list = controller.listar();
        if (!list.isEmpty()) {
            Operacao buscado = list.get(0);
            controller.atualizar(buscado);
            controller.remover(buscado.getIdOperacao());
            controller.criar(op);
        }
    }
}
