package controller;

import dao.api.CofreDao;
import exception.CofreException;
import infra.Logger;
import model.Cofre;

import java.util.List;

public class CofreController {
    private final CofreDao dao;

    public CofreController(CofreDao dao) { this.dao = dao; }

    public void criar(Cofre c) {
        if (c == null || c.getIdCofre() == null) throw new CofreException("Id obrigatorio");
        if (c.getLogin() == null || c.getLogin().isEmpty()) throw new CofreException("Login obrigatorio");
        Logger.info("CofreController.criar");
        dao.create(c);
    }

    public void atualizar(Cofre c) {
        if (c == null || c.getIdCofre() == null) throw new CofreException("Id obrigatorio");
        Logger.info("CofreController.atualizar");
        dao.update(c);
    }

    public void remover(Integer id) {
        if (id == null) throw new CofreException("Id obrigatorio");
        Logger.info("CofreController.remover");
        dao.deleteById(id);
    }

    public Cofre buscarPorId(Integer id) {
        if (id == null) throw new CofreException("Id obrigatorio");
        Logger.info("CofreController.buscarPorId");
        return dao.findById(id);
    }

    public List<Cofre> listar() {
        Logger.info("CofreController.listar");
        return dao.findAll();
    }

    public List<Cofre> listar(int page, int size) {
        Logger.info("CofreController.listarPaginado");
        return dao.findAll(page, size);
    }

    public List<Cofre> buscarPorLogin(String login) {
        Logger.info("CofreController.buscarPorLogin");
        return dao.findByLogin(login);
    }

    public List<Cofre> pesquisar(Cofre filtro) {
        Logger.info("CofreController.pesquisar");
        return dao.search(filtro);
    }

    public List<Cofre> pesquisar(Cofre filtro, int page, int size) {
        Logger.info("CofreController.pesquisarPaginado");
        return dao.search(filtro, page, size);
    }
}
