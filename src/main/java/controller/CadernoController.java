package controller;

import dao.api.CadernoDao;
import exception.CadernoException;
import infra.Logger;
import model.Caderno;

import java.time.LocalDate;
import java.util.List;

public class CadernoController {
    private final CadernoDao dao;

    public CadernoController(CadernoDao dao) { this.dao = dao; }

    public void criar(Caderno caderno) {
        if (caderno == null) throw new CadernoException("Caderno não pode ser nulo");
        if (caderno.getTitulo() == null || caderno.getTitulo().isEmpty()) throw new CadernoException("titulo obrigatorio");
        if (caderno.getComando() == null || caderno.getComando().isEmpty()) throw new CadernoException("comando obrigatorio");
        if (caderno.getData() == null) throw new CadernoException("data obrigatoria");
        Logger.info("CadernoController.criar");
        dao.create(caderno);
    }

    public void atualizar(Caderno caderno) {
        if (caderno == null || caderno.getIdCaderno() == null) throw new CadernoException("Id obrigatorio");
        Logger.info("CadernoController.atualizar");
        dao.update(caderno);
    }

    public void remover(Integer id) {
        if (id == null) throw new CadernoException("Id obrigatorio");
        Logger.info("CadernoController.remover");
        dao.deleteById(id);
    }

    public Caderno buscarPorId(Integer id) {
        if (id == null) throw new CadernoException("Id obrigatorio");
        Logger.info("CadernoController.buscarPorId");
        return dao.findById(id);
    }

    public List<Caderno> listar() {
        Logger.info("CadernoController.listar");
        return dao.findAll();
    }

    public List<Caderno> listar(int page, int size) {
        Logger.info("CadernoController.listarPaginado");
        return dao.findAll(page, size);
    }

    public List<Caderno> buscarPorTitulo(String titulo) {
        Logger.info("CadernoController.buscarPorTitulo");
        return dao.findByTitulo(titulo);
    }

    public List<Caderno> buscarPorData(LocalDate data) {
        Logger.info("CadernoController.buscarPorData");
        return dao.findByData(data);
    }

    public List<Caderno> buscarPorIdUsuario(Integer idUsuario) {
        Logger.info("CadernoController.buscarPorIdUsuario");
        return dao.findByIdUsuario(idUsuario);
    }

    public List<Caderno> buscarPorIdCategoria(Integer idCategoria) {
        Logger.info("CadernoController.buscarPorIdCategoria");
        return dao.findByIdCategoria(idCategoria);
    }

    public List<Caderno> pesquisar(Caderno filtro) {
        Logger.info("CadernoController.pesquisar");
        return dao.search(filtro);
    }

    public List<Caderno> pesquisar(Caderno filtro, int page, int size) {
        Logger.info("CadernoController.pesquisarPaginado");
        return dao.search(filtro, page, size);
    }
}
