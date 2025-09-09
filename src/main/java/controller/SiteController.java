// path: src/main/java/controller/SiteController.java
package controller;

import java.util.List;

import dao.api.SiteDao;
import exception.SiteException;
import infra.Logger;
import model.Site;

public class SiteController {

    private final SiteDao dao;

    public SiteController(SiteDao dao) {
        this.dao = dao;
    }

    public void criar(Site site) {
        Logger.info("SiteController.criar - inicio");
        if (site == null) {
            throw new SiteException("Site não pode ser nulo");
        }
        if (site.getUrl() == null || site.getUrl().isEmpty()) {
            throw new SiteException("URL do Site é obrigatória");
        }
        dao.create(site);
        Logger.info("SiteController.criar - sucesso");
    }

    public Site atualizar(Site site) {
        Logger.info("SiteController.atualizar - inicio");
        if (site == null || site.getIdSite() == null) {
            throw new SiteException("Site ou Id não pode ser nulo");
        }
        if (site.getUrl() == null || site.getUrl().isEmpty()) {
            throw new SiteException("URL do Site é obrigatória");
        }
        Site updated = dao.update(site);
        Logger.info("SiteController.atualizar - sucesso");
        return updated;
    }

    public void remover(Integer id) {
        Logger.info("SiteController.remover - inicio");
        if (id == null) {
            throw new SiteException("Id do Site é obrigatório");
        }
        dao.deleteById(id);
        Logger.info("SiteController.remover - sucesso");
    }

    public Site buscarPorId(Integer id) {
        Logger.info("SiteController.buscarPorId - inicio");
        if (id == null) {
            throw new SiteException("Id do Site é obrigatório");
        }
        Site s = dao.findById(id);
        Logger.info("SiteController.buscarPorId - sucesso");
        return s;
    }

    public Site buscarComLogoPorId(Integer id) {
        Logger.info("SiteController.buscarComLogoPorId - inicio");
        if (id == null) {
            throw new SiteException("Id do Site é obrigatório");
        }
        Site s = dao.findWithLogoById(id);
        Logger.info("SiteController.buscarComLogoPorId - sucesso");
        return s;
    }

    public List<Site> listar() {
        Logger.info("SiteController.listar - inicio");
        List<Site> list = dao.findAll();
        Logger.info("SiteController.listar - sucesso");
        return list;
    }

    public List<Site> listar(int page, int size) {
        Logger.info("SiteController.listar(page) - inicio");
        List<Site> list = dao.findAll(page, size);
        Logger.info("SiteController.listar(page) - sucesso");
        return list;
    }

    public List<Site> buscarPorUrl(String url) {
        Logger.info("SiteController.buscarPorUrl - inicio");
        if (url == null || url.isEmpty()) {
            throw new SiteException("URL não pode ser vazia");
        }
        List<Site> list = dao.findByUrl(url);
        Logger.info("SiteController.buscarPorUrl - sucesso");
        return list;
    }

    public List<Site> buscarPorAtivo(Boolean ativo) {
        Logger.info("SiteController.buscarPorAtivo - inicio");
        if (ativo == null) {
            throw new SiteException("Ativo não pode ser nulo");
        }
        List<Site> list = dao.findByAtivo(ativo);
        Logger.info("SiteController.buscarPorAtivo - sucesso");
        return list;
    }

    public List<Site> buscarPorLogo(byte[] logo) {
        Logger.info("SiteController.buscarPorLogo - inicio");
        if (logo == null) {
            throw new SiteException("Logo não pode ser nulo");
        }
        List<Site> list = dao.findByLogo(logo);
        Logger.info("SiteController.buscarPorLogo - sucesso");
        return list;
    }

    public List<Site> pesquisar(Site filtro) {
        Logger.info("SiteController.pesquisar - inicio");
        List<Site> list = dao.search(filtro);
        Logger.info("SiteController.pesquisar - sucesso");
        return list;
    }

    public List<Site> pesquisar(Site filtro, int page, int size) {
        Logger.info("SiteController.pesquisar(page) - inicio");
        List<Site> list = dao.search(filtro, page, size);
        Logger.info("SiteController.pesquisar(page) - sucesso");
        return list;
    }
}
