// path: src/main/java/controller/LancamentoController.java
package controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import dao.api.LancamentoDao;
import exception.LancamentoException;
import infra.Logger;
import model.Lancamento;

public class LancamentoController {

    private final LancamentoDao dao;

    public LancamentoController(LancamentoDao dao) {
        this.dao = dao;
    }

    public void criar(Lancamento lancamento) {
        Logger.info("LancamentoController.criar - inicio");
        if (lancamento == null) {
            throw new LancamentoException("Lancamento não pode ser nulo");
        }
        if (lancamento.getIdLancamento() == null) {
            throw new LancamentoException("Id do Lancamento é obrigatório");
        }
        if (lancamento.getValor() == null) {
            throw new LancamentoException("Valor do Lancamento é obrigatório");
        }
        if (lancamento.getEvento() == null || lancamento.getEvento().getIdEvento() == null) {
            throw new LancamentoException("Id do Evento do Lancamento é obrigatório");
        }
        dao.create(lancamento);
        Logger.info("LancamentoController.criar - sucesso");
    }

    public Lancamento atualizar(Lancamento lancamento) {
        Logger.info("LancamentoController.atualizar - inicio");
        if (lancamento == null || lancamento.getIdLancamento() == null) {
            throw new LancamentoException("Lancamento ou Id não pode ser nulo");
        }
        if (lancamento.getValor() == null) {
            throw new LancamentoException("Valor do Lancamento é obrigatório");
        }
        Lancamento updated = dao.update(lancamento);
        Logger.info("LancamentoController.atualizar - sucesso");
        return updated;
    }

    public void remover(Integer id) {
        Logger.info("LancamentoController.remover - inicio");
        if (id == null) {
            throw new LancamentoException("Id do Lancamento é obrigatório");
        }
        dao.deleteById(id);
        Logger.info("LancamentoController.remover - sucesso");
    }

    public Lancamento buscarPorId(Integer id) {
        Logger.info("LancamentoController.buscarPorId - inicio");
        if (id == null) {
            throw new LancamentoException("Id do Lancamento é obrigatório");
        }
        Lancamento l = dao.findById(id);
        Logger.info("LancamentoController.buscarPorId - sucesso");
        return l;
    }

    public List<Lancamento> listar() {
        Logger.info("LancamentoController.listar - inicio");
        List<Lancamento> list = dao.findAll();
        Logger.info("LancamentoController.listar - sucesso");
        return list;
    }

    public List<Lancamento> listar(int page, int size) {
        Logger.info("LancamentoController.listar(page) - inicio");
        List<Lancamento> list = dao.findAll(page, size);
        Logger.info("LancamentoController.listar(page) - sucesso");
        return list;
    }

    public List<Lancamento> buscarPorValor(BigDecimal valor) {
        Logger.info("LancamentoController.buscarPorValor - inicio");
        if (valor == null) {
            throw new LancamentoException("Valor não pode ser nulo");
        }
        List<Lancamento> list = dao.findByValor(valor);
        Logger.info("LancamentoController.buscarPorValor - sucesso");
        return list;
    }

    public List<Lancamento> buscarPorFixo(Boolean fixo) {
        Logger.info("LancamentoController.buscarPorFixo - inicio");
        if (fixo == null) {
            throw new LancamentoException("Fixo não pode ser nulo");
        }
        List<Lancamento> list = dao.findByFixo(fixo);
        Logger.info("LancamentoController.buscarPorFixo - sucesso");
        return list;
    }

    public List<Lancamento> buscarPorDataPagamento(LocalDate dataPagamento) {
        Logger.info("LancamentoController.buscarPorDataPagamento - inicio");
        if (dataPagamento == null) {
            throw new LancamentoException("Data de pagamento não pode ser nula");
        }
        List<Lancamento> list = dao.findByDataPagamento(dataPagamento);
        Logger.info("LancamentoController.buscarPorDataPagamento - sucesso");
        return list;
    }

    public List<Lancamento> buscarPorStatus(Integer status) {
        Logger.info("LancamentoController.buscarPorStatus - inicio");
        if (status == null) {
            throw new LancamentoException("Status não pode ser nulo");
        }
        List<Lancamento> list = dao.findByStatus(status);
        Logger.info("LancamentoController.buscarPorStatus - sucesso");
        return list;
    }

    public List<Lancamento> buscarPorIdMovimentacao(Integer idMovimentacao) {
        Logger.info("LancamentoController.buscarPorIdMovimentacao - inicio");
        if (idMovimentacao == null) {
            throw new LancamentoException("Id da movimentação não pode ser nulo");
        }
        List<Lancamento> list = dao.findByIdMovimentacao(idMovimentacao);
        Logger.info("LancamentoController.buscarPorIdMovimentacao - sucesso");
        return list;
    }

    public List<Lancamento> buscarPorIdEvento(Integer idEvento) {
        Logger.info("LancamentoController.buscarPorIdEvento - inicio");
        if (idEvento == null) {
            throw new LancamentoException("Id do evento não pode ser nulo");
        }
        List<Lancamento> list = dao.findByIdEvento(idEvento);
        Logger.info("LancamentoController.buscarPorIdEvento - sucesso");
        return list;
    }

    public List<Lancamento> pesquisar(Lancamento filtro) {
        Logger.info("LancamentoController.pesquisar - inicio");
        List<Lancamento> list = dao.search(filtro);
        Logger.info("LancamentoController.pesquisar - sucesso");
        return list;
    }

    public List<Lancamento> pesquisar(Lancamento filtro, int page, int size) {
        Logger.info("LancamentoController.pesquisar(page) - inicio");
        List<Lancamento> list = dao.search(filtro, page, size);
        Logger.info("LancamentoController.pesquisar(page) - sucesso");
        return list;
    }
}
