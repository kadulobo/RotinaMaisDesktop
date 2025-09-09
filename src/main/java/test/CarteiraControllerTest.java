package test;

import controller.CarteiraController;
import dao.impl.CarteiraDaoNativeImpl;
import model.Carteira;
import controller.UsuarioController;
import dao.impl.UsuarioDaoNativeImpl;

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
        UsuarioController usuarioController = new UsuarioController(new UsuarioDaoNativeImpl());
        carteira.setIdUsuario(TestUtils.getRandom(usuarioController.listar()).getIdUsuario());
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
