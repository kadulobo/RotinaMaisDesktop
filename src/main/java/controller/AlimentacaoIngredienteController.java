package controller;

import java.util.List;

import dao.api.AlimentacaoIngredienteDao;
import exception.AlimentacaoIngredienteException;
import infra.Logger;
import model.AlimentacaoIngrediente;

public class AlimentacaoIngredienteController {
    private final AlimentacaoIngredienteDao dao;

    public AlimentacaoIngredienteController(AlimentacaoIngredienteDao dao) { this.dao = dao; }

    public void criar(AlimentacaoIngrediente e) {
        if (e == null) throw new AlimentacaoIngredienteException("AlimentacaoIngrediente não pode ser nulo");
        if (e.getQuantidade() == null) throw new AlimentacaoIngredienteException("quantidade obrigatorio");
        if (e.getIdAlimentacao() == null) throw new AlimentacaoIngredienteException("idAlimentacao obrigatorio");
        if (e.getIdIngrediente() == null) throw new AlimentacaoIngredienteException("idIngrediente obrigatorio");
        Logger.info("AlimentacaoIngredienteController.criar");
        dao.create(e);
    }

    public void atualizar(AlimentacaoIngrediente e) {
        if (e == null || e.getIdAlimentacaoIngrediente() == null) throw new AlimentacaoIngredienteException("Id obrigatorio");
        Logger.info("AlimentacaoIngredienteController.atualizar");
        dao.update(e);
    }

    public void remover(Integer id) {
        if (id == null) throw new AlimentacaoIngredienteException("Id obrigatorio");
        Logger.info("AlimentacaoIngredienteController.remover");
        dao.deleteById(id);
    }

    public AlimentacaoIngrediente buscarPorId(Integer id) {
        if (id == null) throw new AlimentacaoIngredienteException("Id obrigatorio");
        Logger.info("AlimentacaoIngredienteController.buscarPorId");
        return dao.findById(id);
    }

    public List<AlimentacaoIngrediente> listar() {
        Logger.info("AlimentacaoIngredienteController.listar");
        return dao.findAll();
    }

    public List<AlimentacaoIngrediente> listar(int page, int size) {
        Logger.info("AlimentacaoIngredienteController.listarPaginado");
        return dao.findAll(page, size);
    }

    public List<AlimentacaoIngrediente> buscarPorQuantidade(Integer quantidade) {
        Logger.info("AlimentacaoIngredienteController.buscarPorQuantidade");
        return dao.findByQuantidade(quantidade);
    }

    public List<AlimentacaoIngrediente> buscarPorIdAlimentacao(Integer idAlimentacao) {
        Logger.info("AlimentacaoIngredienteController.buscarPorIdAlimentacao");
        return dao.findByIdAlimentacao(idAlimentacao);
    }

    public List<AlimentacaoIngrediente> buscarPorIdIngrediente(Integer idIngrediente) {
        Logger.info("AlimentacaoIngredienteController.buscarPorIdIngrediente");
        return dao.findByIdIngrediente(idIngrediente);
    }

    public List<AlimentacaoIngrediente> pesquisar(AlimentacaoIngrediente f) {
        Logger.info("AlimentacaoIngredienteController.pesquisar");
        return dao.search(f);
    }

    public List<AlimentacaoIngrediente> pesquisar(AlimentacaoIngrediente f, int page, int size) {
        Logger.info("AlimentacaoIngredienteController.pesquisarPaginado");
        return dao.search(f, page, size);
    }
}