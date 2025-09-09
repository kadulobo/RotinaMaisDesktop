package test;

import controller.FornecedorController;
import dao.impl.FornecedorDaoNativeImpl;
import model.Fornecedor;

import java.util.List;

public class FornecedorControllerTest {
    public static void main(String[] args) {
        FornecedorDaoNativeImpl dao = new FornecedorDaoNativeImpl();
        FornecedorController controller = new FornecedorController(dao);

        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setNome("Fornecedor Exemplo");
        controller.criar(fornecedor);

        List<Fornecedor> list = controller.listar();
        if (!list.isEmpty()) {
            Fornecedor buscado = list.get(0);
            buscado.setNome("Fornecedor Atualizado");
            controller.atualizar(buscado);
            controller.remover(buscado.getIdFornecedor());
            controller.criar(fornecedor);
        }
    }
}
