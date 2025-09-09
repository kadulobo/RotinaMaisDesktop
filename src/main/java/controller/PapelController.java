package controller;

import dao.api.PapelDao;
import exception.PapelException;
import infra.Logger;
import model.Papel;

import java.time.LocalDate;
import java.util.List;

public class PapelController {
    private final PapelDao dao;

    public PapelController(PapelDao dao) { this.dao = dao; }

    public void criar(Papel e) {
        if (e == null || e.getIdPapel() == null) throw new PapelException("Id obrigatorio");
        if (e.getCodigo() == null || e.getCodigo().isEmpty()) throw new PapelException("codigo obrigatorio");
        if (e.getTipo() == null || e.getTipo().isEmpty()) throw new PapelException("tipo obrigatorio");
        if (e.getVencimento() == null) throw new PapelException("vencimento obrigatorio");
        Logger.info("PapelController.criar");
        dao.create(e);
    }

    public void atualizar(Papel e) {
        if (e == null || e.getIdPapel() == null) throw new PapelException("Id obrigatorio");
        Logger.info("PapelController.atualizar");
        dao.update(e);
    }

    public void remover(Integer id) {
        if (id == null) throw new PapelException("Id obrigatorio");
        Logger.info("PapelController.remover");
        dao.deleteById(id);
    }

    public Papel buscarPorId(Integer id) {
        if (id == null) throw new PapelException("Id obrigatorio");
        Logger.info("PapelController.buscarPorId");
        return dao.findById(id);
    }

    public List<Papel> listar() {
        Logger.info("PapelController.listar");
        return dao.findAll();
    }

    public List<Papel> listar(int page, int size) {
        Logger.info("PapelController.listarPaginado");
        return dao.findAll(page, size);
    }

    public List<Papel> buscarPorCodigo(String codigo) {
        Logger.info("PapelController.buscarPorCodigo");
        return dao.findByCodigo(codigo);
    }

    public List<Papel> buscarPorTipo(String tipo) {
        Logger.info("PapelController.buscarPorTipo");
        return dao.findByTipo(tipo);
    }

    public List<Papel> buscarPorVencimento(LocalDate vencimento) {
        Logger.info("PapelController.buscarPorVencimento");
        return dao.findByVencimento(vencimento);
    }

    public List<Papel> pesquisar(Papel f) {
        Logger.info("PapelController.pesquisar");
        return dao.search(f);
    }

    public List<Papel> pesquisar(Papel f, int page, int size) {
        Logger.info("PapelController.pesquisarPaginado");
        return dao.search(f, page, size);
    }
}