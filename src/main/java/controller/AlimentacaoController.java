// path: src/main/java/controller/AlimentacaoController.java
package controller;

import java.util.List;

import dao.api.AlimentacaoDao;
import exception.AlimentacaoException;
import infra.Logger;
import model.Alimentacao;

public class AlimentacaoController {

    private final AlimentacaoDao dao;

    public AlimentacaoController(AlimentacaoDao dao) {
        this.dao = dao;
    }

    public void criar(Alimentacao alimentacao) {
        Logger.info("AlimentacaoController.criar - inicio");
        if (alimentacao == null) {
            throw new AlimentacaoException("Alimentacao não pode ser nula");
        }
        if (alimentacao.getNome() == null || alimentacao.getNome().isEmpty()) {
            throw new AlimentacaoException("Nome da Alimentacao é obrigatório");
        }
        dao.create(alimentacao);
        Logger.info("AlimentacaoController.criar - sucesso");
    }

    public Alimentacao atualizar(Alimentacao alimentacao) {
        Logger.info("AlimentacaoController.atualizar - inicio");
        if (alimentacao == null || alimentacao.getIdAlimentacao() == null) {
            throw new AlimentacaoException("Alimentacao ou Id não pode ser nulo");
        }
        if (alimentacao.getNome() == null || alimentacao.getNome().isEmpty()) {
            throw new AlimentacaoException("Nome da Alimentacao é obrigatório");
        }
        Alimentacao updated = dao.update(alimentacao);
        Logger.info("AlimentacaoController.atualizar - sucesso");
        return updated;
    }

    public void remover(Integer id) {
        Logger.info("AlimentacaoController.remover - inicio");
        if (id == null) {
            throw new AlimentacaoException("Id da Alimentacao é obrigatório");
        }
        dao.deleteById(id);
        Logger.info("AlimentacaoController.remover - sucesso");
    }

    public Alimentacao buscarPorId(Integer id) {
        Logger.info("AlimentacaoController.buscarPorId - inicio");
        if (id == null) {
            throw new AlimentacaoException("Id da Alimentacao é obrigatório");
        }
        Alimentacao a = dao.findById(id);
        Logger.info("AlimentacaoController.buscarPorId - sucesso");
        return a;
    }

    public Alimentacao buscarComVideoPorId(Integer id) {
        Logger.info("AlimentacaoController.buscarComVideoPorId - inicio");
        if (id == null) {
            throw new AlimentacaoException("Id da Alimentacao é obrigatório");
        }
        Alimentacao a = dao.findWithVideoById(id);
        Logger.info("AlimentacaoController.buscarComVideoPorId - sucesso");
        return a;
    }

    public List<Alimentacao> listar() {
        Logger.info("AlimentacaoController.listar - inicio");
        List<Alimentacao> list = dao.findAll();
        Logger.info("AlimentacaoController.listar - sucesso");
        return list;
    }

    public List<Alimentacao> listar(int page, int size) {
        Logger.info("AlimentacaoController.listar(page) - inicio");
        List<Alimentacao> list = dao.findAll(page, size);
        Logger.info("AlimentacaoController.listar(page) - sucesso");
        return list;
    }

    public List<Alimentacao> buscarPorStatus(Integer status) {
        Logger.info("AlimentacaoController.buscarPorStatus - inicio");
        if (status == null) {
            throw new AlimentacaoException("Status não pode ser nulo");
        }
        List<Alimentacao> list = dao.findByStatus(status);
        Logger.info("AlimentacaoController.buscarPorStatus - sucesso");
        return list;
    }

    public List<Alimentacao> buscarPorNome(String nome) {
        Logger.info("AlimentacaoController.buscarPorNome - inicio");
        if (nome == null || nome.isEmpty()) {
            throw new AlimentacaoException("Nome não pode ser vazio");
        }
        List<Alimentacao> list = dao.findByNome(nome);
        Logger.info("AlimentacaoController.buscarPorNome - sucesso");
        return list;
    }

    public List<Alimentacao> buscarPorLink(String link) {
        Logger.info("AlimentacaoController.buscarPorLink - inicio");
        if (link == null || link.isEmpty()) {
            throw new AlimentacaoException("Link não pode ser vazio");
        }
        List<Alimentacao> list = dao.findByLink(link);
        Logger.info("AlimentacaoController.buscarPorLink - sucesso");
        return list;
    }

    public List<Alimentacao> buscarPorVideo(byte[] video) {
        Logger.info("AlimentacaoController.buscarPorVideo - inicio");
        if (video == null) {
            throw new AlimentacaoException("Video não pode ser nulo");
        }
        List<Alimentacao> list = dao.findByVideo(video);
        Logger.info("AlimentacaoController.buscarPorVideo - sucesso");
        return list;
    }

    public List<Alimentacao> buscarPorPreparo(String preparo) {
        Logger.info("AlimentacaoController.buscarPorPreparo - inicio");
        if (preparo == null || preparo.isEmpty()) {
            throw new AlimentacaoException("Preparo não pode ser vazio");
        }
        List<Alimentacao> list = dao.findByPreparo(preparo);
        Logger.info("AlimentacaoController.buscarPorPreparo - sucesso");
        return list;
    }

    public List<Alimentacao> buscarPorIdRotina(Integer idRotina) {
        Logger.info("AlimentacaoController.buscarPorIdRotina - inicio");
        if (idRotina == null) {
            throw new AlimentacaoException("Id da rotina não pode ser nulo");
        }
        List<Alimentacao> list = dao.findByIdRotina(idRotina);
        Logger.info("AlimentacaoController.buscarPorIdRotina - sucesso");
        return list;
    }

    public List<Alimentacao> pesquisar(Alimentacao filtro) {
        Logger.info("AlimentacaoController.pesquisar - inicio");
        List<Alimentacao> list = dao.search(filtro);
        Logger.info("AlimentacaoController.pesquisar - sucesso");
        return list;
    }

    public List<Alimentacao> pesquisar(Alimentacao filtro, int page, int size) {
        Logger.info("AlimentacaoController.pesquisar(page) - inicio");
        List<Alimentacao> list = dao.search(filtro, page, size);
        Logger.info("AlimentacaoController.pesquisar(page) - sucesso");
        return list;
    }
}
