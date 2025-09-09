package main;

import controller.SiteObjetoController;
import dao.impl.SiteObjetoDaoNativeImpl;
import model.SiteObjeto;

import java.util.List;

public class SiteObjetoControllerTest {
    public static void main(String[] args) {
        SiteObjetoDaoNativeImpl dao = new SiteObjetoDaoNativeImpl();
        SiteObjetoController controller = new SiteObjetoController(dao);

        SiteObjeto so = new SiteObjeto();
        so.setIdSite(1);
        so.setIdObjeto(1);
        controller.criar(so);

        List<SiteObjeto> list = controller.listar();
        if (!list.isEmpty()) {
            SiteObjeto buscado = list.get(0);
            controller.atualizar(buscado);
            controller.remover(buscado.getIdSiteObjeto());
            controller.criar(so);
        }
    }
}
