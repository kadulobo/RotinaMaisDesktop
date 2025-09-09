package main;

import controller.AlimentacaoIngredienteController;
import dao.impl.AlimentacaoIngredienteDaoNativeImpl;
import model.AlimentacaoIngrediente;

import java.util.List;

public class AlimentacaoIngredienteControllerTest {
    public static void main(String[] args) {
        AlimentacaoIngredienteDaoNativeImpl dao = new AlimentacaoIngredienteDaoNativeImpl();
        AlimentacaoIngredienteController controller = new AlimentacaoIngredienteController(dao);

        AlimentacaoIngrediente ai = new AlimentacaoIngrediente();
        ai.setQuantidade(1);
        ai.setIdAlimentacao(1);
        ai.setIdIngrediente(1);
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
