package test;

import controller.IngredienteController;
import dao.impl.IngredienteDaoNativeImpl;
import model.Ingrediente;

import java.util.List;

public class IngredienteControllerTest {
    public static void main(String[] args) {
        IngredienteDaoNativeImpl dao = new IngredienteDaoNativeImpl();
        IngredienteController controller = new IngredienteController(dao);

        Ingrediente ingrediente = new Ingrediente();
        ingrediente.setNome("Ingrediente Exemplo");
        controller.criar(ingrediente);

        List<Ingrediente> list = controller.listar();
        if (!list.isEmpty()) {
            Ingrediente buscado = list.get(0);
            buscado.setNome("Ingrediente Atualizado");
            controller.atualizar(buscado);
            controller.remover(buscado.getIdIngrediente());
            controller.criar(ingrediente);
        }
    }
}
