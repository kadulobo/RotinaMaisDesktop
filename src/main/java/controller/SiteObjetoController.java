package controller;

import dao.api.SiteObjetoDao;
import exception.SiteObjetoException;
import infra.Logger;
import model.SiteObjeto;

import java.util.List;

public class SiteObjetoController {
    private final {dao} dao;

    public SiteObjetoController(SiteObjetoDao dao) { this.dao = dao; }

    public void criar(SiteObjeto e) {
        if (e == null || e.getIdSiteObjeto() == null) throw new SiteObjetoException("Id obrigatorio");
        if (e.getIdSite() == null) throw new SiteObjetoException("idSite obrigatorio");
        if (e.getIdObjeto() == null) throw new SiteObjetoException("idObjeto obrigatorio");
        Logger.info("SiteObjetoController.criar");
        dao.create(e);
    }

    public void atualizar(SiteObjeto e) {
        if (e == null || e.getIdSiteObjeto() == null) throw new SiteObjetoException("Id obrigatorio");
        Logger.info("SiteObjetoController.atualizar");
        dao.update(e);
    }

    public void remover(Integer id) {
        if (id == null) throw new SiteObjetoException("Id obrigatorio");
        Logger.info("SiteObjetoController.remover");
        dao.deleteById(id);
    }

    public SiteObjeto buscarPorId(Integer id) {
        if (id == null) throw new SiteObjetoException("Id obrigatorio");
        Logger.info("SiteObjetoController.buscarPorId");
        return dao.findById(id);
    }

    public List<SiteObjeto> listar() {
        Logger.info("SiteObjetoController.listar");
        return dao.findAll();
    }

    public List<SiteObjeto> listar(int page, int size) {
        Logger.info("SiteObjetoController.listarPaginado");
        return dao.findAll(page, size);
    }

    public List<SiteObjeto> buscarPorIdSite(Integer idSite) {
        Logger.info("SiteObjetoController.buscarPorIdSite");
        return dao.findByIdSite(idSite);
    }

    public List<SiteObjeto> buscarPorIdObjeto(Integer idObjeto) {
        Logger.info("SiteObjetoController.buscarPorIdObjeto");
        return dao.findByIdObjeto(idObjeto);
    }

    public List<SiteObjeto> pesquisar(SiteObjeto f) {
        Logger.info("SiteObjetoController.pesquisar");
        return dao.search(f);
    }

    public List<SiteObjeto> pesquisar(SiteObjeto f, int page, int size) {
        Logger.info("SiteObjetoController.pesquisarPaginado");
        return dao.search(f, page, size);
    }
}