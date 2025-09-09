package test;

import controller.SiteObjetoController;
import dao.impl.SiteObjetoDaoNativeImpl;
import model.SiteObjeto;
import controller.SiteController;
import controller.ObjetoController;
import dao.impl.SiteDaoNativeImpl;
import dao.impl.ObjetoDaoNativeImpl;

import java.util.List;

public class SiteObjetoControllerTest {
    public static void main(String[] args) {
        SiteObjetoDaoNativeImpl dao = new SiteObjetoDaoNativeImpl();
        SiteObjetoController controller = new SiteObjetoController(dao);

        SiteObjeto so = new SiteObjeto();
        SiteController siteController = new SiteController(new SiteDaoNativeImpl());
        ObjetoController objetoController = new ObjetoController(new ObjetoDaoNativeImpl());
        so.setIdSite(TestUtils.getRandom(siteController.listar()).getIdSite());
        so.setIdObjeto(TestUtils.getRandom(objetoController.listar()).getIdObjeto());
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
