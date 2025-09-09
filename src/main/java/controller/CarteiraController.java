package controller;

import dao.api.CarteiraDao;
import exception.CarteiraException;
import infra.Logger;
import model.Carteira;

import java.util.List;

public class CarteiraController {
    private final {dao} dao;

    public CarteiraController(CarteiraDao dao) { this.dao = dao; }

    public void criar(Carteira e) {
        if (e == null || e.getIdCarteira() == null) throw new CarteiraException("Id obrigatorio");
        if (e.getNome() == null || e.getNome().isEmpty()) throw new CarteiraException("nome obrigatorio");
        if (e.getTipo() == null || e.getTipo().isEmpty()) throw new CarteiraException("tipo obrigatorio");
        if (e.getDataInicio() == null) throw new CarteiraException("dataInicio obrigatorio");
        if (e.getIdUsuario() == null) throw new CarteiraException("idUsuario obrigatorio");
        Logger.info("CarteiraController.criar");
        dao.create(e);
    }

    public void atualizar(Carteira e) {
        if (e == null || e.getIdCarteira() == null) throw new CarteiraException("Id obrigatorio");
        Logger.info("CarteiraController.atualizar");
        dao.update(e);
    }

    public void remover(Integer id) {
        if (id == null) throw new CarteiraException("Id obrigatorio");
        Logger.info("CarteiraController.remover");
        dao.deleteById(id);
    }

    public Carteira buscarPorId(Integer id) {
        if (id == null) throw new CarteiraException("Id obrigatorio");
        Logger.info("CarteiraController.buscarPorId");
        return dao.findById(id);
    }

    public List<Carteira> listar() {
        Logger.info("CarteiraController.listar");
        return dao.findAll();
    }

    public List<Carteira> listar(int page, int size) {
        Logger.info("CarteiraController.listarPaginado");
        return dao.findAll(page, size);
    }

    public List<Carteira> buscarPorNome(String nome) {
        Logger.info("CarteiraController.buscarPorNome");
        return dao.findByNome(nome);
    }

    public List<Carteira> buscarPorTipo(String tipo) {
        Logger.info("CarteiraController.buscarPorTipo");
        return dao.findByTipo(tipo);
    }

    public List<Carteira> buscarPorDataInicio(LocalDate dataInicio) {
        Logger.info("CarteiraController.buscarPorDataInicio");
        return dao.findByDataInicio(dataInicio);
    }

    public List<Carteira> buscarPorIdUsuario(Integer idUsuario) {
        Logger.info("CarteiraController.buscarPorIdUsuario");
        return dao.findByIdUsuario(idUsuario);
    }

    public List<Carteira> pesquisar(Carteira f) {
        Logger.info("CarteiraController.pesquisar");
        return dao.search(f);
    }

    public List<Carteira> pesquisar(Carteira f, int page, int size) {
        Logger.info("CarteiraController.pesquisarPaginado");
        return dao.search(f, page, size);
    }
}