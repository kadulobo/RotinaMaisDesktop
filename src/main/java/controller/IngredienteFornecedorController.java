package controller;

import dao.api.IngredienteFornecedorDao;
import exception.IngredienteFornecedorException;
import infra.Logger;
import model.IngredienteFornecedor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class IngredienteFornecedorController {
    private final IngredienteFornecedorDao dao;

    public IngredienteFornecedorController(IngredienteFornecedorDao dao) { this.dao = dao; }

    public void criar(IngredienteFornecedor e) {
        if (e == null || e.getIdFornecedorIngrediente() == null) throw new IngredienteFornecedorException("Id obrigatorio");
        if (e.getValor() == null) throw new IngredienteFornecedorException("valor obrigatorio");
        if (e.getData() == null) throw new IngredienteFornecedorException("data obrigatorio");
        if (e.getIdFornecedor() == null) throw new IngredienteFornecedorException("idFornecedor obrigatorio");
        if (e.getIdIngrediente() == null) throw new IngredienteFornecedorException("idIngrediente obrigatorio");
        Logger.info("IngredienteFornecedorController.criar");
        dao.create(e);
    }

    public void atualizar(IngredienteFornecedor e) {
        if (e == null || e.getIdFornecedorIngrediente() == null) throw new IngredienteFornecedorException("Id obrigatorio");
        Logger.info("IngredienteFornecedorController.atualizar");
        dao.update(e);
    }

    public void remover(Integer id) {
        if (id == null) throw new IngredienteFornecedorException("Id obrigatorio");
        Logger.info("IngredienteFornecedorController.remover");
        dao.deleteById(id);
    }

    public IngredienteFornecedor buscarPorId(Integer id) {
        if (id == null) throw new IngredienteFornecedorException("Id obrigatorio");
        Logger.info("IngredienteFornecedorController.buscarPorId");
        return dao.findById(id);
    }

    public List<IngredienteFornecedor> listar() {
        Logger.info("IngredienteFornecedorController.listar");
        return dao.findAll();
    }

    public List<IngredienteFornecedor> listar(int page, int size) {
        Logger.info("IngredienteFornecedorController.listarPaginado");
        return dao.findAll(page, size);
    }

    public List<IngredienteFornecedor> buscarPorValor(BigDecimal valor) {
        Logger.info("IngredienteFornecedorController.buscarPorValor");
        return dao.findByValor(valor);
    }

    public List<IngredienteFornecedor> buscarPorData(LocalDate data) {
        Logger.info("IngredienteFornecedorController.buscarPorData");
        return dao.findByData(data);
    }

    public List<IngredienteFornecedor> buscarPorIdFornecedor(Integer idFornecedor) {
        Logger.info("IngredienteFornecedorController.buscarPorIdFornecedor");
        return dao.findByIdFornecedor(idFornecedor);
    }

    public List<IngredienteFornecedor> buscarPorIdIngrediente(Integer idIngrediente) {
        Logger.info("IngredienteFornecedorController.buscarPorIdIngrediente");
        return dao.findByIdIngrediente(idIngrediente);
    }

    public List<IngredienteFornecedor> pesquisar(IngredienteFornecedor f) {
        Logger.info("IngredienteFornecedorController.pesquisar");
        return dao.search(f);
    }

    public List<IngredienteFornecedor> pesquisar(IngredienteFornecedor f, int page, int size) {
        Logger.info("IngredienteFornecedorController.pesquisarPaginado");
        return dao.search(f, page, size);
    }
}