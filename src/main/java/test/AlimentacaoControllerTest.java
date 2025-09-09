package test;

import controller.AlimentacaoController;
import dao.impl.AlimentacaoDaoNativeImpl;
import model.Alimentacao;

import java.util.List;

public class AlimentacaoControllerTest {
    public static void main(String[] args) {
        AlimentacaoDaoNativeImpl dao = new AlimentacaoDaoNativeImpl();
        AlimentacaoController controller = new AlimentacaoController(dao);

        Alimentacao alimentacao = new Alimentacao();
        alimentacao.setNome("Alimentação Exemplo");
        controller.criar(alimentacao);

        List<Alimentacao> list = controller.listar();
        if (!list.isEmpty()) {
            Alimentacao buscado = list.get(0);
            buscado.setNome("Alimentação Atualizada");
            controller.atualizar(buscado);
            controller.remover(buscado.getIdAlimentacao());
            controller.criar(alimentacao);
        }
    }
}
