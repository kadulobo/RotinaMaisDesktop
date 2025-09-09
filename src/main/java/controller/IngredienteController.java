// path: src/main/java/controller/IngredienteController.java
package controller;

import java.util.List;

import dao.api.IngredienteDao;
import exception.IngredienteException;
import infra.Logger;
import model.Ingrediente;

public class IngredienteController {

    private final IngredienteDao dao;

    public IngredienteController(IngredienteDao dao) {
        this.dao = dao;
    }

    public void criar(Ingrediente ingrediente) {
        Logger.info("IngredienteController.criar - inicio");
        if (ingrediente == null) {
            throw new IngredienteException("Ingrediente não pode ser nulo");
        }
        if (ingrediente.getIdIngrediente() == null) {
            throw new IngredienteException("Id do Ingrediente é obrigatório");
        }
        if (ingrediente.getNome() == null || ingrediente.getNome().isEmpty()) {
            throw new IngredienteException("Nome do Ingrediente é obrigatório");
        }
        dao.create(ingrediente);
        Logger.info("IngredienteController.criar - sucesso");
    }

    public Ingrediente atualizar(Ingrediente ingrediente) {
        Logger.info("IngredienteController.atualizar - inicio");
        if (ingrediente == null || ingrediente.getIdIngrediente() == null) {
            throw new IngredienteException("Ingrediente ou Id não pode ser nulo");
        }
        if (ingrediente.getNome() == null || ingrediente.getNome().isEmpty()) {
            throw new IngredienteException("Nome do Ingrediente é obrigatório");
        }
        Ingrediente updated = dao.update(ingrediente);
        Logger.info("IngredienteController.atualizar - sucesso");
        return updated;
    }

    public void remover(Integer id) {
        Logger.info("IngredienteController.remover - inicio");
        if (id == null) {
            throw new IngredienteException("Id do Ingrediente é obrigatório");
        }
        dao.deleteById(id);
        Logger.info("IngredienteController.remover - sucesso");
    }

    public Ingrediente buscarPorId(Integer id) {
        Logger.info("IngredienteController.buscarPorId - inicio");
        if (id == null) {
            throw new IngredienteException("Id do Ingrediente é obrigatório");
        }
        Ingrediente i = dao.findById(id);
        Logger.info("IngredienteController.buscarPorId - sucesso");
        return i;
    }

    public Ingrediente buscarComFotoPorId(Integer id) {
        Logger.info("IngredienteController.buscarComFotoPorId - inicio");
        if (id == null) {
            throw new IngredienteException("Id do Ingrediente é obrigatório");
        }
        Ingrediente i = dao.findWithFotoById(id);
        Logger.info("IngredienteController.buscarComFotoPorId - sucesso");
        return i;
    }

    public List<Ingrediente> listar() {
        Logger.info("IngredienteController.listar - inicio");
        List<Ingrediente> list = dao.findAll();
        Logger.info("IngredienteController.listar - sucesso");
        return list;
    }

    public List<Ingrediente> listar(int page, int size) {
        Logger.info("IngredienteController.listar(page) - inicio");
        List<Ingrediente> list = dao.findAll(page, size);
        Logger.info("IngredienteController.listar(page) - sucesso");
        return list;
    }

    public List<Ingrediente> buscarPorFoto(byte[] foto) {
        Logger.info("IngredienteController.buscarPorFoto - inicio");
        if (foto == null) {
            throw new IngredienteException("Foto não pode ser nula");
        }
        List<Ingrediente> list = dao.findByFoto(foto);
        Logger.info("IngredienteController.buscarPorFoto - sucesso");
        return list;
    }

    public List<Ingrediente> buscarPorNome(String nome) {
        Logger.info("IngredienteController.buscarPorNome - inicio");
        if (nome == null || nome.isEmpty()) {
            throw new IngredienteException("Nome não pode ser vazio");
        }
        List<Ingrediente> list = dao.findByNome(nome);
        Logger.info("IngredienteController.buscarPorNome - sucesso");
        return list;
    }

    public List<Ingrediente> buscarPorDescricao(String descricao) {
        Logger.info("IngredienteController.buscarPorDescricao - inicio");
        if (descricao == null || descricao.isEmpty()) {
            throw new IngredienteException("Descrição não pode ser vazia");
        }
        List<Ingrediente> list = dao.findByDescricao(descricao);
        Logger.info("IngredienteController.buscarPorDescricao - sucesso");
        return list;
    }

    public List<Ingrediente> pesquisar(Ingrediente filtro) {
        Logger.info("IngredienteController.pesquisar - inicio");
        List<Ingrediente> list = dao.search(filtro);
        Logger.info("IngredienteController.pesquisar - sucesso");
        return list;
    }

    public List<Ingrediente> pesquisar(Ingrediente filtro, int page, int size) {
        Logger.info("IngredienteController.pesquisar(page) - inicio");
        List<Ingrediente> list = dao.search(filtro, page, size);
        Logger.info("IngredienteController.pesquisar(page) - sucesso");
        return list;
    }
}
