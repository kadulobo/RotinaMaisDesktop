package controller;

import dao.api.MonitoramentoObjetoDao;
import exception.MonitoramentoObjetoException;
import infra.Logger;
import model.MonitoramentoObjeto;

import java.time.LocalDate;
import java.util.List;

public class MonitoramentoObjetoController {
    private final MonitoramentoObjetoDao dao;

    public MonitoramentoObjetoController(MonitoramentoObjetoDao dao) { this.dao = dao; }

    public void criar(MonitoramentoObjeto e) {
        if (e == null) throw new MonitoramentoObjetoException("MonitoramentoObjeto não pode ser nulo");
        if (e.getData() == null) throw new MonitoramentoObjetoException("data obrigatorio");
        if (e.getIdMonitoramento() == null) throw new MonitoramentoObjetoException("idMonitoramento obrigatorio");
        if (e.getIdObjeto() == null) throw new MonitoramentoObjetoException("idObjeto obrigatorio");
        Logger.info("MonitoramentoObjetoController.criar");
        dao.create(e);
    }

    public void atualizar(MonitoramentoObjeto e) {
        if (e == null || e.getIdMonitoramentoObjeto() == null) throw new MonitoramentoObjetoException("Id obrigatorio");
        Logger.info("MonitoramentoObjetoController.atualizar");
        dao.update(e);
    }

    public void remover(Integer id) {
        if (id == null) throw new MonitoramentoObjetoException("Id obrigatorio");
        Logger.info("MonitoramentoObjetoController.remover");
        dao.deleteById(id);
    }

    public MonitoramentoObjeto buscarPorId(Integer id) {
        if (id == null) throw new MonitoramentoObjetoException("Id obrigatorio");
        Logger.info("MonitoramentoObjetoController.buscarPorId");
        return dao.findById(id);
    }

    public List<MonitoramentoObjeto> listar() {
        Logger.info("MonitoramentoObjetoController.listar");
        return dao.findAll();
    }

    public List<MonitoramentoObjeto> listar(int page, int size) {
        Logger.info("MonitoramentoObjetoController.listarPaginado");
        return dao.findAll(page, size);
    }

    public List<MonitoramentoObjeto> buscarPorData(LocalDate data) {
        Logger.info("MonitoramentoObjetoController.buscarPorData");
        return dao.findByData(data);
    }

    public List<MonitoramentoObjeto> buscarPorIdMonitoramento(Integer idMonitoramento) {
        Logger.info("MonitoramentoObjetoController.buscarPorIdMonitoramento");
        return dao.findByIdMonitoramento(idMonitoramento);
    }

    public List<MonitoramentoObjeto> buscarPorIdObjeto(Integer idObjeto) {
        Logger.info("MonitoramentoObjetoController.buscarPorIdObjeto");
        return dao.findByIdObjeto(idObjeto);
    }

    public List<MonitoramentoObjeto> pesquisar(MonitoramentoObjeto f) {
        Logger.info("MonitoramentoObjetoController.pesquisar");
        return dao.search(f);
    }

    public List<MonitoramentoObjeto> pesquisar(MonitoramentoObjeto f, int page, int size) {
        Logger.info("MonitoramentoObjetoController.pesquisarPaginado");
        return dao.search(f, page, size);
    }
}