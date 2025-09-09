package test;

import controller.IngredienteFornecedorController;
import dao.impl.IngredienteFornecedorDaoNativeImpl;
import model.IngredienteFornecedor;
import controller.FornecedorController;
import controller.IngredienteController;
import dao.impl.FornecedorDaoNativeImpl;
import dao.impl.IngredienteDaoNativeImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class IngredienteFornecedorControllerTest {
    public static void main(String[] args) {
        IngredienteFornecedorDaoNativeImpl dao = new IngredienteFornecedorDaoNativeImpl();
        IngredienteFornecedorController controller = new IngredienteFornecedorController(dao);

        IngredienteFornecedor rel = new IngredienteFornecedor();
        rel.setValor(BigDecimal.ONE);
        rel.setData(LocalDate.now());
        FornecedorController fornecedorController = new FornecedorController(new FornecedorDaoNativeImpl());
        IngredienteController ingredienteController = new IngredienteController(new IngredienteDaoNativeImpl());
        rel.setIdFornecedor(TestUtils.getRandom(fornecedorController.listar()).getIdFornecedor());
        rel.setIdIngrediente(TestUtils.getRandom(ingredienteController.listar()).getIdIngrediente());
        controller.criar(rel);

        List<IngredienteFornecedor> list = controller.listar();
        if (!list.isEmpty()) {
            IngredienteFornecedor buscado = list.get(0);
            controller.atualizar(buscado);
            controller.remover(buscado.getIdFornecedorIngrediente());
            controller.criar(rel);
        }
    }
}
