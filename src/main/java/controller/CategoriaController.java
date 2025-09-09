// path: src/main/java/controller/CategoriaController.java
package controller;

import java.time.LocalDate;
import java.util.List;

import dao.api.CategoriaDao;
import exception.CategoriaException;
import infra.Logger;
import model.Categoria;

public class CategoriaController {

    private final CategoriaDao dao;

    public CategoriaController(CategoriaDao dao) {
        this.dao = dao;
    }

    public void criar(Categoria categoria) {
        Logger.info("CategoriaController.criar - inicio");
        if (categoria == null) {
            throw new CategoriaException("Categoria não pode ser nula");
        }
        if (categoria.getNome() == null || categoria.getNome().isEmpty()) {
            throw new CategoriaException("Nome da Categoria é obrigatório");
        }
        dao.create(categoria);
        Logger.info("CategoriaController.criar - sucesso");
    }

    public Categoria atualizar(Categoria categoria) {
        Logger.info("CategoriaController.atualizar - inicio");
        if (categoria == null || categoria.getIdCategoria() == null) {
            throw new CategoriaException("Categoria ou Id não pode ser nulo");
        }
        if (categoria.getNome() == null || categoria.getNome().isEmpty()) {
            throw new CategoriaException("Nome da Categoria é obrigatório");
        }
        Categoria updated = dao.update(categoria);
        Logger.info("CategoriaController.atualizar - sucesso");
        return updated;
    }

    public void remover(Integer id) {
        Logger.info("CategoriaController.remover - inicio");
        if (id == null) {
            throw new CategoriaException("Id da Categoria é obrigatório");
        }
        dao.deleteById(id);
        Logger.info("CategoriaController.remover - sucesso");
    }

    public Categoria buscarPorId(Integer id) {
        Logger.info("CategoriaController.buscarPorId - inicio");
        if (id == null) {
            throw new CategoriaException("Id da Categoria é obrigatório");
        }
        Categoria c = dao.findById(id);
        Logger.info("CategoriaController.buscarPorId - sucesso");
        return c;
    }

    public List<Categoria> listar() {
        Logger.info("CategoriaController.listar - inicio");
        List<Categoria> list = dao.findAll();
        Logger.info("CategoriaController.listar - sucesso");
        return list;
    }

    public List<Categoria> listar(int page, int size) {
        Logger.info("CategoriaController.listar(page) - inicio");
        List<Categoria> list = dao.findAll(page, size);
        Logger.info("CategoriaController.listar(page) - sucesso");
        return list;
    }

    public List<Categoria> buscarPorNome(String nome) {
        Logger.info("CategoriaController.buscarPorNome - inicio");
        if (nome == null || nome.isEmpty()) {
            throw new CategoriaException("Nome não pode ser vazio");
        }
        List<Categoria> list = dao.findByNome(nome);
        Logger.info("CategoriaController.buscarPorNome - sucesso");
        return list;
    }

    public List<Categoria> buscarPorDescricao(String descricao) {
        Logger.info("CategoriaController.buscarPorDescricao - inicio");
        if (descricao == null || descricao.isEmpty()) {
            throw new CategoriaException("Descrição não pode ser vazia");
        }
        List<Categoria> list = dao.findByDescricao(descricao);
        Logger.info("CategoriaController.buscarPorDescricao - sucesso");
        return list;
    }

    public List<Categoria> buscarPorFoto(byte[] foto) {
        Logger.info("CategoriaController.buscarPorFoto - inicio");
        if (foto == null) {
            throw new CategoriaException("Foto não pode ser nula");
        }
        List<Categoria> list = dao.findByFoto(foto);
        Logger.info("CategoriaController.buscarPorFoto - sucesso");
        return list;
    }

    public List<Categoria> buscarPorDataCriacao(LocalDate data) {
        Logger.info("CategoriaController.buscarPorDataCriacao - inicio");
        if (data == null) {
            throw new CategoriaException("Data de criação não pode ser nula");
        }
        List<Categoria> list = dao.findByDataCriacao(data);
        Logger.info("CategoriaController.buscarPorDataCriacao - sucesso");
        return list;
    }

    public List<Categoria> pesquisar(Categoria filtro) {
        Logger.info("CategoriaController.pesquisar - inicio");
        List<Categoria> list = dao.search(filtro);
        Logger.info("CategoriaController.pesquisar - sucesso");
        return list;
    }

    public List<Categoria> pesquisar(Categoria filtro, int page, int size) {
        Logger.info("CategoriaController.pesquisar(page) - inicio");
        List<Categoria> list = dao.search(filtro, page, size);
        Logger.info("CategoriaController.pesquisar(page) - sucesso");
        return list;
    }
}
