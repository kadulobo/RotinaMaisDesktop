package main;

import controller.DocumentoController;
import dao.impl.DocumentoDaoNativeImpl;
import model.Documento;

import java.util.List;

public class DocumentoControllerTest {
    public static void main(String[] args) {
        DocumentoDaoNativeImpl dao = new DocumentoDaoNativeImpl();
        DocumentoController controller = new DocumentoController(dao);

        Documento documento = new Documento();
        documento.setNome("Documento Exemplo");
        controller.criar(documento);

        List<Documento> list = controller.listar();
        if (!list.isEmpty()) {
            Documento buscado = list.get(0);
            buscado.setNome("Documento Atualizado");
            controller.atualizar(buscado);
            controller.remover(buscado.getIdDocumento());
            controller.criar(documento);
        }
    }
}
