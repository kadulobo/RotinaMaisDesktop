package controller;

import dao.api.TreinoExercicioDao;
import exception.TreinoExercicioException;
import infra.Logger;
import model.TreinoExercicio;

import java.util.List;

public class TreinoExercicioController {
    private final TreinoExercicioDao dao;

    public TreinoExercicioController(TreinoExercicioDao dao) { this.dao = dao; }

    public void criar(TreinoExercicio e) {
        if (e == null) throw new TreinoExercicioException("TreinoExercicio não pode ser nulo");
        if (e.getQtdRepeticao() == null) throw new TreinoExercicioException("qtdRepeticao obrigatorio");
        if (e.getTempoDescanso() == null || e.getTempoDescanso().isEmpty()) throw new TreinoExercicioException("tempoDescanso obrigatorio");
        if (e.getOrdem() == null) throw new TreinoExercicioException("ordem obrigatorio");
        if (e.getFeito() == null) throw new TreinoExercicioException("feito obrigatorio");
        if (e.getIdExercicio() == null) throw new TreinoExercicioException("idExercicio obrigatorio");
        if (e.getIdTreino() == null) throw new TreinoExercicioException("idTreino obrigatorio");
        Logger.info("TreinoExercicioController.criar");
        dao.create(e);
    }

    public void atualizar(TreinoExercicio e) {
        if (e == null || e.getIdTreinoExercicio() == null) throw new TreinoExercicioException("Id obrigatorio");
        Logger.info("TreinoExercicioController.atualizar");
        dao.update(e);
    }

    public void remover(Integer id) {
        if (id == null) throw new TreinoExercicioException("Id obrigatorio");
        Logger.info("TreinoExercicioController.remover");
        dao.deleteById(id);
    }

    public TreinoExercicio buscarPorId(Integer id) {
        if (id == null) throw new TreinoExercicioException("Id obrigatorio");
        Logger.info("TreinoExercicioController.buscarPorId");
        return dao.findById(id);
    }

    public List<TreinoExercicio> listar() {
        Logger.info("TreinoExercicioController.listar");
        return dao.findAll();
    }

    public List<TreinoExercicio> listar(int page, int size) {
        Logger.info("TreinoExercicioController.listarPaginado");
        return dao.findAll(page, size);
    }

    public List<TreinoExercicio> buscarPorQtdRepeticao(Integer qtdRepeticao) {
        Logger.info("TreinoExercicioController.buscarPorQtdRepeticao");
        return dao.findByQtdRepeticao(qtdRepeticao);
    }

    public List<TreinoExercicio> buscarPorTempoDescanso(String tempoDescanso) {
        Logger.info("TreinoExercicioController.buscarPorTempoDescanso");
        return dao.findByTempoDescanso(tempoDescanso);
    }

    public List<TreinoExercicio> buscarPorOrdem(Integer ordem) {
        Logger.info("TreinoExercicioController.buscarPorOrdem");
        return dao.findByOrdem(ordem);
    }

    public List<TreinoExercicio> buscarPorFeito(Boolean feito) {
        Logger.info("TreinoExercicioController.buscarPorFeito");
        return dao.findByFeito(feito);
    }

    public List<TreinoExercicio> buscarPorIdExercicio(Integer idExercicio) {
        Logger.info("TreinoExercicioController.buscarPorIdExercicio");
        return dao.findByIdExercicio(idExercicio);
    }

    public List<TreinoExercicio> buscarPorIdTreino(Integer idTreino) {
        Logger.info("TreinoExercicioController.buscarPorIdTreino");
        return dao.findByIdTreino(idTreino);
    }

    public List<TreinoExercicio> pesquisar(TreinoExercicio f) {
        Logger.info("TreinoExercicioController.pesquisar");
        return dao.search(f);
    }

    public List<TreinoExercicio> pesquisar(TreinoExercicio f, int page, int size) {
        Logger.info("TreinoExercicioController.pesquisarPaginado");
        return dao.search(f, page, size);
    }
}