package main;

import controller.CarteiraController;
import dao.impl.CarteiraDaoNativeImpl;
import model.Carteira;

import java.time.LocalDate;
import java.util.List;

public class CarteiraControllerTest {
    public static void main(String[] args) {
        CarteiraDaoNativeImpl dao = new CarteiraDaoNativeImpl();
        CarteiraController controller = new CarteiraController(dao);

        Carteira carteira = new Carteira();
        carteira.setNome("Carteira Exemplo");
        carteira.setTipo("T");
        carteira.setDataInicio(LocalDate.now());
        carteira.setIdUsuario(1);
        controller.criar(carteira);

        List<Carteira> list = controller.listar();
        if (!list.isEmpty()) {
            Carteira buscado = list.get(0);
            buscado.setNome("Carteira Atualizada");
            controller.atualizar(buscado);
            controller.remover(buscado.getIdCarteira());
            controller.criar(carteira);
        }
    }
}
