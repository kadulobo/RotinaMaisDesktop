package test;

import controller.EventoController;
import dao.impl.EventoDaoNativeImpl;
import model.Evento;

import java.util.List;

public class EventoControllerTest {
    public static void main(String[] args) {
        EventoDaoNativeImpl dao = new EventoDaoNativeImpl();
        EventoController controller = new EventoController(dao);

        Evento evento = new Evento();
        evento.setNome("Evento Exemplo");
        controller.criar(evento);

        List<Evento> list = controller.listar();
        if (!list.isEmpty()) {
            Evento buscado = list.get(0);
            buscado.setNome("Evento Atualizado");
            controller.atualizar(buscado);
            controller.remover(buscado.getIdEvento());
            controller.criar(evento);
        }
    }
}
