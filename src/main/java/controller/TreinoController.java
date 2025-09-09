// path: src/main/java/controller/TreinoController.java
package controller;

import java.util.List;

import dao.api.TreinoDao;
import exception.TreinoException;
import infra.Logger;
import model.Treino;

public class TreinoController {

    private final TreinoDao dao;

    public TreinoController(TreinoDao dao) {
        this.dao = dao;
    }

    public void criar(Treino treino) {
        Logger.info("TreinoController.criar - inicio");
        if (treino == null) {
            throw new TreinoException("Treino não pode ser nulo");
        }
        if (treino.getNome() == null || treino.getNome().isEmpty()) {
            throw new TreinoException("Nome do Treino é obrigatório");
        }
        dao.create(treino);
        Logger.info("TreinoController.criar - sucesso");
    }

    public Treino atualizar(Treino treino) {
        Logger.info("TreinoController.atualizar - inicio");
        if (treino == null || treino.getIdTreino() == null) {
            throw new TreinoException("Treino ou Id não pode ser nulo");
        }
        if (treino.getNome() == null || treino.getNome().isEmpty()) {
            throw new TreinoException("Nome do Treino é obrigatório");
        }
        Treino updated = dao.update(treino);
        Logger.info("TreinoController.atualizar - sucesso");
        return updated;
    }

    public void remover(Integer id) {
        Logger.info("TreinoController.remover - inicio");
        if (id == null) {
            throw new TreinoException("Id do Treino é obrigatório");
        }
        dao.deleteById(id);
        Logger.info("TreinoController.remover - sucesso");
    }

    public Treino buscarPorId(Integer id) {
        Logger.info("TreinoController.buscarPorId - inicio");
        if (id == null) {
            throw new TreinoException("Id do Treino é obrigatório");
        }
        Treino t = dao.findById(id);
        Logger.info("TreinoController.buscarPorId - sucesso");
        return t;
    }

    public List<Treino> listar() {
        Logger.info("TreinoController.listar - inicio");
        List<Treino> list = dao.findAll();
        Logger.info("TreinoController.listar - sucesso");
        return list;
    }

    public List<Treino> listar(int page, int size) {
        Logger.info("TreinoController.listar(page) - inicio");
        List<Treino> list = dao.findAll(page, size);
        Logger.info("TreinoController.listar(page) - sucesso");
        return list;
    }

    public List<Treino> buscarPorNome(String nome) {
        Logger.info("TreinoController.buscarPorNome - inicio");
        if (nome == null || nome.isEmpty()) {
            throw new TreinoException("Nome não pode ser vazio");
        }
        List<Treino> list = dao.findByNome(nome);
        Logger.info("TreinoController.buscarPorNome - sucesso");
        return list;
    }

    public List<Treino> buscarPorClasse(String classe) {
        Logger.info("TreinoController.buscarPorClasse - inicio");
        if (classe == null || classe.isEmpty()) {
            throw new TreinoException("Classe não pode ser vazia");
        }
        List<Treino> list = dao.findByClasse(classe);
        Logger.info("TreinoController.buscarPorClasse - sucesso");
        return list;
    }

    public List<Treino> buscarPorIdRotina(Integer idRotina) {
        Logger.info("TreinoController.buscarPorIdRotina - inicio");
        if (idRotina == null) {
            throw new TreinoException("Id da rotina não pode ser nulo");
        }
        List<Treino> list = dao.findByIdRotina(idRotina);
        Logger.info("TreinoController.buscarPorIdRotina - sucesso");
        return list;
    }

    public List<Treino> pesquisar(Treino filtro) {
        Logger.info("TreinoController.pesquisar - inicio");
        List<Treino> list = dao.search(filtro);
        Logger.info("TreinoController.pesquisar - sucesso");
        return list;
    }

    public List<Treino> pesquisar(Treino filtro, int page, int size) {
        Logger.info("TreinoController.pesquisar(page) - inicio");
        List<Treino> list = dao.search(filtro, page, size);
        Logger.info("TreinoController.pesquisar(page) - sucesso");
        return list;
    }
}
