package test;

import controller.OperacaoController;
import dao.impl.OperacaoDaoNativeImpl;
import model.Operacao;
import controller.CarteiraController;
import controller.PapelController;
import dao.impl.CarteiraDaoNativeImpl;
import dao.impl.PapelDaoNativeImpl;

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
        CarteiraController carteiraController = new CarteiraController(new CarteiraDaoNativeImpl());
        PapelController papelController = new PapelController(new PapelDaoNativeImpl());
        op.setIdCarteira(TestUtils.getRandom(carteiraController.listar()).getIdCarteira());
        op.setIdPapel(TestUtils.getRandom(papelController.listar()).getIdPapel());
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
