package test;

import controller.SiteController;
import dao.impl.SiteDaoNativeImpl;
import model.Site;

import java.util.List;

public class SiteControllerTest {
    public static void main(String[] args) {
        SiteDaoNativeImpl dao = new SiteDaoNativeImpl();
        SiteController controller = new SiteController(dao);

        Site site = new Site();
        site.setUrl("http://exemplo.com");
        controller.criar(site);

        List<Site> list = controller.listar();
        if (!list.isEmpty()) {
            Site buscado = list.get(0);
            buscado.setUrl("http://atualizado.com");
            controller.atualizar(buscado);
            controller.remover(buscado.getIdSite());
            controller.criar(site);
        }
    }
}
