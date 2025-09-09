package controller;

import dao.api.MetaDao;
import exception.MetaException;
import infra.Logger;
import model.Meta;

import java.util.List;

public class MetaController {
    private final MetaDao dao;

    public MetaController(MetaDao dao) { this.dao = dao; }

    public void criar(Meta meta) {
        if (meta == null || meta.getIdMeta() == null) throw new MetaException("Id obrigatorio");
        Logger.info("MetaController.criar");
        dao.create(meta);
    }

    public void atualizar(Meta meta) {
        if (meta == null || meta.getIdMeta() == null) throw new MetaException("Id obrigatorio");
        Logger.info("MetaController.atualizar");
        dao.update(meta);
    }

    public void remover(Integer id) {
        if (id == null) throw new MetaException("Id obrigatorio");
        Logger.info("MetaController.remover");
        dao.deleteById(id);
    }

    public Meta buscarPorId(Integer id) {
        if (id == null) throw new MetaException("Id obrigatorio");
        Logger.info("MetaController.buscarPorId");
        return dao.findById(id);
    }

    public Meta buscarComFotoPorId(Integer id) {
        if (id == null) throw new MetaException("Id obrigatorio");
        Logger.info("MetaController.buscarComFotoPorId");
        return dao.findWithFotoById(id);
    }

    public List<Meta> listar() {
        Logger.info("MetaController.listar");
        return dao.findAll();
    }

    public List<Meta> listar(int page, int size) {
        Logger.info("MetaController.listarPaginado");
        return dao.findAll(page, size);
    }

    public List<Meta> buscarPorStatus(Integer status) {
        Logger.info("MetaController.buscarPorStatus");
        return dao.findByStatus(status);
    }

    public List<Meta> pesquisar(Meta filtro) {
        Logger.info("MetaController.pesquisar");
        return dao.search(filtro);
    }

    public List<Meta> pesquisar(Meta filtro, int page, int size) {
        Logger.info("MetaController.pesquisarPaginado");
        return dao.search(filtro, page, size);
    }
}
