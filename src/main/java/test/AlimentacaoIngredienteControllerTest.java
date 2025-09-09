package test;

import controller.AlimentacaoIngredienteController;
import dao.impl.AlimentacaoIngredienteDaoNativeImpl;
import model.AlimentacaoIngrediente;
import controller.AlimentacaoController;
import controller.IngredienteController;
import dao.impl.AlimentacaoDaoNativeImpl;
import dao.impl.IngredienteDaoNativeImpl;

import java.util.List;

public class AlimentacaoIngredienteControllerTest {
    public static void main(String[] args) {
        AlimentacaoIngredienteDaoNativeImpl dao = new AlimentacaoIngredienteDaoNativeImpl();
        AlimentacaoIngredienteController controller = new AlimentacaoIngredienteController(dao);

        AlimentacaoIngrediente ai = new AlimentacaoIngrediente();
        ai.setQuantidade(1);
        AlimentacaoController alimentacaoController = new AlimentacaoController(new AlimentacaoDaoNativeImpl());
        IngredienteController ingredienteController = new IngredienteController(new IngredienteDaoNativeImpl());
        ai.setIdAlimentacao(TestUtils.getRandom(alimentacaoController.listar()).getIdAlimentacao());
        ai.setIdIngrediente(TestUtils.getRandom(ingredienteController.listar()).getIdIngrediente());
        controller.criar(ai);

        List<AlimentacaoIngrediente> list = controller.listar();
        if (!list.isEmpty()) {
            AlimentacaoIngrediente buscado = list.get(0);
            controller.atualizar(buscado);
            controller.remover(buscado.getIdAlimentacaoIngrediente());
            controller.criar(ai);
        }
    }
}
