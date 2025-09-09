// path: src/main/java/controller/MovimentacaoController.java
package controller;

import java.math.BigDecimal;
import java.util.List;

import dao.api.MovimentacaoDao;
import exception.MovimentacaoException;
import infra.Logger;
import model.Movimentacao;

public class MovimentacaoController {

    private final MovimentacaoDao dao;

    public MovimentacaoController(MovimentacaoDao dao) {
        this.dao = dao;
    }

    public void criar(Movimentacao movimentacao) {
        Logger.info("MovimentacaoController.criar - inicio");
        if (movimentacao == null) {
            throw new MovimentacaoException("Movimentacao não pode ser nula");
        }
        if (movimentacao.getTipo() == null) {
            throw new MovimentacaoException("Tipo da Movimentacao é obrigatório");
        }
        dao.create(movimentacao);
        Logger.info("MovimentacaoController.criar - sucesso");
    }

    public Movimentacao atualizar(Movimentacao movimentacao) {
        Logger.info("MovimentacaoController.atualizar - inicio");
        if (movimentacao == null || movimentacao.getIdMovimentacao() == null) {
            throw new MovimentacaoException("Movimentacao ou Id não pode ser nulo");
        }
        if (movimentacao.getTipo() == null) {
            throw new MovimentacaoException("Tipo da Movimentacao é obrigatório");
        }
        Movimentacao updated = dao.update(movimentacao);
        Logger.info("MovimentacaoController.atualizar - sucesso");
        return updated;
    }

    public void remover(Integer id) {
        Logger.info("MovimentacaoController.remover - inicio");
        if (id == null) {
            throw new MovimentacaoException("Id da Movimentacao é obrigatório");
        }
        dao.deleteById(id);
        Logger.info("MovimentacaoController.remover - sucesso");
    }

    public Movimentacao buscarPorId(Integer id) {
        Logger.info("MovimentacaoController.buscarPorId - inicio");
        if (id == null) {
            throw new MovimentacaoException("Id da Movimentacao é obrigatório");
        }
        Movimentacao m = dao.findById(id);
        Logger.info("MovimentacaoController.buscarPorId - sucesso");
        return m;
    }

    public List<Movimentacao> listar() {
        Logger.info("MovimentacaoController.listar - inicio");
        List<Movimentacao> list = dao.findAll();
        Logger.info("MovimentacaoController.listar - sucesso");
        return list;
    }

    public List<Movimentacao> listar(int page, int size) {
        Logger.info("MovimentacaoController.listar(page) - inicio");
        List<Movimentacao> list = dao.findAll(page, size);
        Logger.info("MovimentacaoController.listar(page) - sucesso");
        return list;
    }

    public List<Movimentacao> buscarPorDesconto(BigDecimal desconto) {
        Logger.info("MovimentacaoController.buscarPorDesconto - inicio");
        if (desconto == null) {
            throw new MovimentacaoException("Desconto não pode ser nulo");
        }
        List<Movimentacao> list = dao.findByDesconto(desconto);
        Logger.info("MovimentacaoController.buscarPorDesconto - sucesso");
        return list;
    }

    public List<Movimentacao> buscarPorVantagem(BigDecimal vantagem) {
        Logger.info("MovimentacaoController.buscarPorVantagem - inicio");
        if (vantagem == null) {
            throw new MovimentacaoException("Vantagem não pode ser nula");
        }
        List<Movimentacao> list = dao.findByVantagem(vantagem);
        Logger.info("MovimentacaoController.buscarPorVantagem - sucesso");
        return list;
    }

    public List<Movimentacao> buscarPorLiquido(BigDecimal liquido) {
        Logger.info("MovimentacaoController.buscarPorLiquido - inicio");
        if (liquido == null) {
            throw new MovimentacaoException("Liquido não pode ser nulo");
        }
        List<Movimentacao> list = dao.findByLiquido(liquido);
        Logger.info("MovimentacaoController.buscarPorLiquido - sucesso");
        return list;
    }

    public List<Movimentacao> buscarPorTipo(Integer tipo) {
        Logger.info("MovimentacaoController.buscarPorTipo - inicio");
        if (tipo == null) {
            throw new MovimentacaoException("Tipo não pode ser nulo");
        }
        List<Movimentacao> list = dao.findByTipo(tipo);
        Logger.info("MovimentacaoController.buscarPorTipo - sucesso");
        return list;
    }

    public List<Movimentacao> buscarPorStatus(Integer status) {
        Logger.info("MovimentacaoController.buscarPorStatus - inicio");
        if (status == null) {
            throw new MovimentacaoException("Status não pode ser nulo");
        }
        List<Movimentacao> list = dao.findByStatus(status);
        Logger.info("MovimentacaoController.buscarPorStatus - sucesso");
        return list;
    }

    public List<Movimentacao> buscarPorPonto(Integer ponto) {
        Logger.info("MovimentacaoController.buscarPorPonto - inicio");
        if (ponto == null) {
            throw new MovimentacaoException("Ponto não pode ser nulo");
        }
        List<Movimentacao> list = dao.findByPonto(ponto);
        Logger.info("MovimentacaoController.buscarPorPonto - sucesso");
        return list;
    }

    public List<Movimentacao> buscarPorIdUsuario(Integer idUsuario) {
        Logger.info("MovimentacaoController.buscarPorIdUsuario - inicio");
        if (idUsuario == null) {
            throw new MovimentacaoException("Id do usuário não pode ser nulo");
        }
        List<Movimentacao> list = dao.findByIdUsuario(idUsuario);
        Logger.info("MovimentacaoController.buscarPorIdUsuario - sucesso");
        return list;
    }

    public List<Movimentacao> buscarPorIdCaixa(Integer idCaixa) {
        Logger.info("MovimentacaoController.buscarPorIdCaixa - inicio");
        if (idCaixa == null) {
            throw new MovimentacaoException("Id do caixa não pode ser nulo");
        }
        List<Movimentacao> list = dao.findByIdCaixa(idCaixa);
        Logger.info("MovimentacaoController.buscarPorIdCaixa - sucesso");
        return list;
    }

    public List<Movimentacao> buscarPorIdPeriodo(Integer idPeriodo) {
        Logger.info("MovimentacaoController.buscarPorIdPeriodo - inicio");
        if (idPeriodo == null) {
            throw new MovimentacaoException("Id do período não pode ser nulo");
        }
        List<Movimentacao> list = dao.findByIdPeriodo(idPeriodo);
        Logger.info("MovimentacaoController.buscarPorIdPeriodo - sucesso");
        return list;
    }

    public List<Movimentacao> pesquisar(Movimentacao filtro) {
        Logger.info("MovimentacaoController.pesquisar - inicio");
        List<Movimentacao> list = dao.search(filtro);
        Logger.info("MovimentacaoController.pesquisar - sucesso");
        return list;
    }

    public List<Movimentacao> pesquisar(Movimentacao filtro, int page, int size) {
        Logger.info("MovimentacaoController.pesquisar(page) - inicio");
        List<Movimentacao> list = dao.search(filtro, page, size);
        Logger.info("MovimentacaoController.pesquisar(page) - sucesso");
        return list;
    }
}
