package controller;

import dao.api.OperacaoDao;
import exception.OperacaoException;
import infra.Logger;
import model.Operacao;

import java.util.List;

public class OperacaoController {
    private final {dao} dao;

    public OperacaoController(OperacaoDao dao) { this.dao = dao; }

    public void criar(Operacao e) {
        if (e == null || e.getIdOperacao() == null) throw new OperacaoException("Id obrigatorio");
        if (e.getFechamento() == null) throw new OperacaoException("fechamento obrigatorio");
        if (e.getTempoOperacao() == null) throw new OperacaoException("tempoOperacao obrigatorio");
        if (e.getQtdCompra() == null) throw new OperacaoException("qtdCompra obrigatorio");
        if (e.getAbertura() == null) throw new OperacaoException("abertura obrigatorio");
        if (e.getQtdVenda() == null) throw new OperacaoException("qtdVenda obrigatorio");
        if (e.getLado() == null || e.getLado().isEmpty()) throw new OperacaoException("lado obrigatorio");
        if (e.getPrecoCompra() == null) throw new OperacaoException("precoCompra obrigatorio");
        if (e.getPrecoVenda() == null) throw new OperacaoException("precoVenda obrigatorio");
        if (e.getPrecoMedio() == null) throw new OperacaoException("precoMedio obrigatorio");
        if (e.getResIntervalo() == null || e.getResIntervalo().isEmpty()) throw new OperacaoException("resIntervalo obrigatorio");
        if (e.getNumeroOperacao() == null) throw new OperacaoException("numeroOperacao obrigatorio");
        if (e.getResOperacao() == null || e.getResOperacao().isEmpty()) throw new OperacaoException("resOperacao obrigatorio");
        if (e.getDrawdon() == null) throw new OperacaoException("drawdon obrigatorio");
        if (e.getGanhoMax() == null) throw new OperacaoException("ganhoMax obrigatorio");
        if (e.getPerdaMax() == null) throw new OperacaoException("perdaMax obrigatorio");
        if (e.getTet() == null || e.getTet().isEmpty()) throw new OperacaoException("tet obrigatorio");
        if (e.getTotal() == null) throw new OperacaoException("total obrigatorio");
        if (e.getIdCarteira() == null) throw new OperacaoException("idCarteira obrigatorio");
        if (e.getIdPapel() == null) throw new OperacaoException("idPapel obrigatorio");
        Logger.info("OperacaoController.criar");
        dao.create(e);
    }

    public void atualizar(Operacao e) {
        if (e == null || e.getIdOperacao() == null) throw new OperacaoException("Id obrigatorio");
        Logger.info("OperacaoController.atualizar");
        dao.update(e);
    }

    public void remover(Integer id) {
        if (id == null) throw new OperacaoException("Id obrigatorio");
        Logger.info("OperacaoController.remover");
        dao.deleteById(id);
    }

    public Operacao buscarPorId(Integer id) {
        if (id == null) throw new OperacaoException("Id obrigatorio");
        Logger.info("OperacaoController.buscarPorId");
        return dao.findById(id);
    }

    public List<Operacao> listar() {
        Logger.info("OperacaoController.listar");
        return dao.findAll();
    }

    public List<Operacao> listar(int page, int size) {
        Logger.info("OperacaoController.listarPaginado");
        return dao.findAll(page, size);
    }

    public List<Operacao> buscarPorFechamento(BigDecimal fechamento) {
        Logger.info("OperacaoController.buscarPorFechamento");
        return dao.findByFechamento(fechamento);
    }

    public List<Operacao> buscarPorTempoOperacao(LocalTime tempoOperacao) {
        Logger.info("OperacaoController.buscarPorTempoOperacao");
        return dao.findByTempoOperacao(tempoOperacao);
    }

    public List<Operacao> buscarPorQtdCompra(Integer qtdCompra) {
        Logger.info("OperacaoController.buscarPorQtdCompra");
        return dao.findByQtdCompra(qtdCompra);
    }

    public List<Operacao> buscarPorAbertura(BigDecimal abertura) {
        Logger.info("OperacaoController.buscarPorAbertura");
        return dao.findByAbertura(abertura);
    }

    public List<Operacao> buscarPorQtdVenda(Integer qtdVenda) {
        Logger.info("OperacaoController.buscarPorQtdVenda");
        return dao.findByQtdVenda(qtdVenda);
    }

    public List<Operacao> buscarPorLado(String lado) {
        Logger.info("OperacaoController.buscarPorLado");
        return dao.findByLado(lado);
    }

    public List<Operacao> buscarPorPrecoCompra(BigDecimal precoCompra) {
        Logger.info("OperacaoController.buscarPorPrecoCompra");
        return dao.findByPrecoCompra(precoCompra);
    }

    public List<Operacao> buscarPorPrecoVenda(BigDecimal precoVenda) {
        Logger.info("OperacaoController.buscarPorPrecoVenda");
        return dao.findByPrecoVenda(precoVenda);
    }

    public List<Operacao> buscarPorPrecoMedio(BigDecimal precoMedio) {
        Logger.info("OperacaoController.buscarPorPrecoMedio");
        return dao.findByPrecoMedio(precoMedio);
    }

    public List<Operacao> buscarPorResIntervalo(String resIntervalo) {
        Logger.info("OperacaoController.buscarPorResIntervalo");
        return dao.findByResIntervalo(resIntervalo);
    }

    public List<Operacao> buscarPorNumeroOperacao(BigDecimal numeroOperacao) {
        Logger.info("OperacaoController.buscarPorNumeroOperacao");
        return dao.findByNumeroOperacao(numeroOperacao);
    }

    public List<Operacao> buscarPorResOperacao(String resOperacao) {
        Logger.info("OperacaoController.buscarPorResOperacao");
        return dao.findByResOperacao(resOperacao);
    }

    public List<Operacao> buscarPorDrawdon(BigDecimal drawdon) {
        Logger.info("OperacaoController.buscarPorDrawdon");
        return dao.findByDrawdon(drawdon);
    }

    public List<Operacao> buscarPorGanhoMax(BigDecimal ganhoMax) {
        Logger.info("OperacaoController.buscarPorGanhoMax");
        return dao.findByGanhoMax(ganhoMax);
    }

    public List<Operacao> buscarPorPerdaMax(BigDecimal perdaMax) {
        Logger.info("OperacaoController.buscarPorPerdaMax");
        return dao.findByPerdaMax(perdaMax);
    }

    public List<Operacao> buscarPorTet(String tet) {
        Logger.info("OperacaoController.buscarPorTet");
        return dao.findByTet(tet);
    }

    public List<Operacao> buscarPorTotal(BigDecimal total) {
        Logger.info("OperacaoController.buscarPorTotal");
        return dao.findByTotal(total);
    }

    public List<Operacao> buscarPorIdCarteira(Integer idCarteira) {
        Logger.info("OperacaoController.buscarPorIdCarteira");
        return dao.findByIdCarteira(idCarteira);
    }

    public List<Operacao> buscarPorIdPapel(Integer idPapel) {
        Logger.info("OperacaoController.buscarPorIdPapel");
        return dao.findByIdPapel(idPapel);
    }

    public List<Operacao> pesquisar(Operacao f) {
        Logger.info("OperacaoController.pesquisar");
        return dao.search(f);
    }

    public List<Operacao> pesquisar(Operacao f, int page, int size) {
        Logger.info("OperacaoController.pesquisarPaginado");
        return dao.search(f, page, size);
    }
}