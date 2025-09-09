// path: src/main/java/controller/MonitoramentoController.java
package controller;

import java.util.List;

import dao.api.MonitoramentoDao;
import exception.MonitoramentoException;
import infra.Logger;
import model.Monitoramento;

public class MonitoramentoController {

    private final MonitoramentoDao dao;

    public MonitoramentoController(MonitoramentoDao dao) {
        this.dao = dao;
    }

    public void criar(Monitoramento monitoramento) {
        Logger.info("MonitoramentoController.criar - inicio");
        if (monitoramento == null) {
            throw new MonitoramentoException("Monitoramento não pode ser nulo");
        }
        if (monitoramento.getNome() == null || monitoramento.getNome().isEmpty()) {
            throw new MonitoramentoException("Nome do Monitoramento é obrigatório");
        }
        dao.create(monitoramento);
        Logger.info("MonitoramentoController.criar - sucesso");
    }

    public Monitoramento atualizar(Monitoramento monitoramento) {
        Logger.info("MonitoramentoController.atualizar - inicio");
        if (monitoramento == null || monitoramento.getIdMonitoramento() == null) {
            throw new MonitoramentoException("Monitoramento ou Id não pode ser nulo");
        }
        if (monitoramento.getNome() == null || monitoramento.getNome().isEmpty()) {
            throw new MonitoramentoException("Nome do Monitoramento é obrigatório");
        }
        Monitoramento updated = dao.update(monitoramento);
        Logger.info("MonitoramentoController.atualizar - sucesso");
        return updated;
    }

    public void remover(Integer id) {
        Logger.info("MonitoramentoController.remover - inicio");
        if (id == null) {
            throw new MonitoramentoException("Id do Monitoramento é obrigatório");
        }
        dao.deleteById(id);
        Logger.info("MonitoramentoController.remover - sucesso");
    }

    public Monitoramento buscarPorId(Integer id) {
        Logger.info("MonitoramentoController.buscarPorId - inicio");
        if (id == null) {
            throw new MonitoramentoException("Id do Monitoramento é obrigatório");
        }
        Monitoramento m = dao.findById(id);
        Logger.info("MonitoramentoController.buscarPorId - sucesso");
        return m;
    }

    public Monitoramento buscarComFotoPorId(Integer id) {
        Logger.info("MonitoramentoController.buscarComFotoPorId - inicio");
        if (id == null) {
            throw new MonitoramentoException("Id do Monitoramento é obrigatório");
        }
        Monitoramento m = dao.findWithFotoById(id);
        Logger.info("MonitoramentoController.buscarComFotoPorId - sucesso");
        return m;
    }

    public List<Monitoramento> listar() {
        Logger.info("MonitoramentoController.listar - inicio");
        List<Monitoramento> list = dao.findAll();
        Logger.info("MonitoramentoController.listar - sucesso");
        return list;
    }

    public List<Monitoramento> listar(int page, int size) {
        Logger.info("MonitoramentoController.listar(page) - inicio");
        List<Monitoramento> list = dao.findAll(page, size);
        Logger.info("MonitoramentoController.listar(page) - sucesso");
        return list;
    }

    public List<Monitoramento> buscarPorStatus(Integer status) {
        Logger.info("MonitoramentoController.buscarPorStatus - inicio");
        if (status == null) {
            throw new MonitoramentoException("Status não pode ser nulo");
        }
        List<Monitoramento> list = dao.findByStatus(status);
        Logger.info("MonitoramentoController.buscarPorStatus - sucesso");
        return list;
    }

    public List<Monitoramento> buscarPorNome(String nome) {
        Logger.info("MonitoramentoController.buscarPorNome - inicio");
        if (nome == null || nome.isEmpty()) {
            throw new MonitoramentoException("Nome não pode ser vazio");
        }
        List<Monitoramento> list = dao.findByNome(nome);
        Logger.info("MonitoramentoController.buscarPorNome - sucesso");
        return list;
    }

    public List<Monitoramento> buscarPorDescricao(String descricao) {
        Logger.info("MonitoramentoController.buscarPorDescricao - inicio");
        if (descricao == null || descricao.isEmpty()) {
            throw new MonitoramentoException("Descrição não pode ser vazia");
        }
        List<Monitoramento> list = dao.findByDescricao(descricao);
        Logger.info("MonitoramentoController.buscarPorDescricao - sucesso");
        return list;
    }

    public List<Monitoramento> buscarPorFoto(byte[] foto) {
        Logger.info("MonitoramentoController.buscarPorFoto - inicio");
        if (foto == null) {
            throw new MonitoramentoException("Foto não pode ser nula");
        }
        List<Monitoramento> list = dao.findByFoto(foto);
        Logger.info("MonitoramentoController.buscarPorFoto - sucesso");
        return list;
    }

    public List<Monitoramento> buscarPorIdPeriodo(Integer idPeriodo) {
        Logger.info("MonitoramentoController.buscarPorIdPeriodo - inicio");
        if (idPeriodo == null) {
            throw new MonitoramentoException("Id do período não pode ser nulo");
        }
        List<Monitoramento> list = dao.findByIdPeriodo(idPeriodo);
        Logger.info("MonitoramentoController.buscarPorIdPeriodo - sucesso");
        return list;
    }

    public List<Monitoramento> pesquisar(Monitoramento filtro) {
        Logger.info("MonitoramentoController.pesquisar - inicio");
        List<Monitoramento> list = dao.search(filtro);
        Logger.info("MonitoramentoController.pesquisar - sucesso");
        return list;
    }

    public List<Monitoramento> pesquisar(Monitoramento filtro, int page, int size) {
        Logger.info("MonitoramentoController.pesquisar(page) - inicio");
        List<Monitoramento> list = dao.search(filtro, page, size);
        Logger.info("MonitoramentoController.pesquisar(page) - sucesso");
        return list;
    }
}
