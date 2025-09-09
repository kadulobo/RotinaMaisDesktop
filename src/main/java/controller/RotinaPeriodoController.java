package controller;

import dao.api.RotinaPeriodoDao;
import exception.RotinaPeriodoException;
import infra.Logger;
import model.RotinaPeriodo;

import java.util.List;

public class RotinaPeriodoController {
    private final RotinaPeriodoDao dao;

    public RotinaPeriodoController(RotinaPeriodoDao dao) { this.dao = dao; }

    public void criar(RotinaPeriodo e) {
        if (e == null) throw new RotinaPeriodoException("RotinaPeriodo não pode ser nulo");
        if (e.getIdRotina() == null) throw new RotinaPeriodoException("idRotina obrigatorio");
        if (e.getIdPeriodo() == null) throw new RotinaPeriodoException("idPeriodo obrigatorio");
        Logger.info("RotinaPeriodoController.criar");
        dao.create(e);
    }

    public void atualizar(RotinaPeriodo e) {
        if (e == null || e.getIdRotinaPeriodo() == null) throw new RotinaPeriodoException("Id obrigatorio");
        Logger.info("RotinaPeriodoController.atualizar");
        dao.update(e);
    }

    public void remover(Integer id) {
        if (id == null) throw new RotinaPeriodoException("Id obrigatorio");
        Logger.info("RotinaPeriodoController.remover");
        dao.deleteById(id);
    }

    public RotinaPeriodo buscarPorId(Integer id) {
        if (id == null) throw new RotinaPeriodoException("Id obrigatorio");
        Logger.info("RotinaPeriodoController.buscarPorId");
        return dao.findById(id);
    }

    public List<RotinaPeriodo> listar() {
        Logger.info("RotinaPeriodoController.listar");
        return dao.findAll();
    }

    public List<RotinaPeriodo> listar(int page, int size) {
        Logger.info("RotinaPeriodoController.listarPaginado");
        return dao.findAll(page, size);
    }

    public List<RotinaPeriodo> buscarPorIdRotina(Integer idRotina) {
        Logger.info("RotinaPeriodoController.buscarPorIdRotina");
        return dao.findByIdRotina(idRotina);
    }

    public List<RotinaPeriodo> buscarPorIdPeriodo(Integer idPeriodo) {
        Logger.info("RotinaPeriodoController.buscarPorIdPeriodo");
        return dao.findByIdPeriodo(idPeriodo);
    }

    public List<RotinaPeriodo> pesquisar(RotinaPeriodo f) {
        Logger.info("RotinaPeriodoController.pesquisar");
        return dao.search(f);
    }

    public List<RotinaPeriodo> pesquisar(RotinaPeriodo f, int page, int size) {
        Logger.info("RotinaPeriodoController.pesquisarPaginado");
        return dao.search(f, page, size);
    }
}