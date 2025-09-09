// path: src/main/java/controller/FornecedorController.java
package controller;

import java.util.List;

import dao.api.FornecedorDao;
import exception.FornecedorException;
import infra.Logger;
import model.Fornecedor;

public class FornecedorController {

    private final FornecedorDao dao;

    public FornecedorController(FornecedorDao dao) {
        this.dao = dao;
    }

    public void criar(Fornecedor fornecedor) {
        Logger.info("FornecedorController.criar - inicio");
        if (fornecedor == null) {
            throw new FornecedorException("Fornecedor não pode ser nulo");
        }
        if (fornecedor.getIdFornecedor() == null) {
            throw new FornecedorException("Id do Fornecedor é obrigatório");
        }
        if (fornecedor.getNome() == null || fornecedor.getNome().isEmpty()) {
            throw new FornecedorException("Nome do Fornecedor é obrigatório");
        }
        dao.create(fornecedor);
        Logger.info("FornecedorController.criar - sucesso");
    }

    public Fornecedor atualizar(Fornecedor fornecedor) {
        Logger.info("FornecedorController.atualizar - inicio");
        if (fornecedor == null || fornecedor.getIdFornecedor() == null) {
            throw new FornecedorException("Fornecedor ou Id não pode ser nulo");
        }
        if (fornecedor.getNome() == null || fornecedor.getNome().isEmpty()) {
            throw new FornecedorException("Nome do Fornecedor é obrigatório");
        }
        Fornecedor updated = dao.update(fornecedor);
        Logger.info("FornecedorController.atualizar - sucesso");
        return updated;
    }

    public void remover(Integer id) {
        Logger.info("FornecedorController.remover - inicio");
        if (id == null) {
            throw new FornecedorException("Id do Fornecedor é obrigatório");
        }
        dao.deleteById(id);
        Logger.info("FornecedorController.remover - sucesso");
    }

    public Fornecedor buscarPorId(Integer id) {
        Logger.info("FornecedorController.buscarPorId - inicio");
        if (id == null) {
            throw new FornecedorException("Id do Fornecedor é obrigatório");
        }
        Fornecedor f = dao.findById(id);
        Logger.info("FornecedorController.buscarPorId - sucesso");
        return f;
    }

    public Fornecedor buscarComFotoPorId(Integer id) {
        Logger.info("FornecedorController.buscarComFotoPorId - inicio");
        if (id == null) {
            throw new FornecedorException("Id do Fornecedor é obrigatório");
        }
        Fornecedor f = dao.findWithFotoById(id);
        Logger.info("FornecedorController.buscarComFotoPorId - sucesso");
        return f;
    }

    public List<Fornecedor> listar() {
        Logger.info("FornecedorController.listar - inicio");
        List<Fornecedor> list = dao.findAll();
        Logger.info("FornecedorController.listar - sucesso");
        return list;
    }

    public List<Fornecedor> listar(int page, int size) {
        Logger.info("FornecedorController.listar(page) - inicio");
        List<Fornecedor> list = dao.findAll(page, size);
        Logger.info("FornecedorController.listar(page) - sucesso");
        return list;
    }

    public List<Fornecedor> buscarPorNome(String nome) {
        Logger.info("FornecedorController.buscarPorNome - inicio");
        if (nome == null || nome.isEmpty()) {
            throw new FornecedorException("Nome não pode ser vazio");
        }
        List<Fornecedor> list = dao.findByNome(nome);
        Logger.info("FornecedorController.buscarPorNome - sucesso");
        return list;
    }

    public List<Fornecedor> buscarPorFoto(byte[] foto) {
        Logger.info("FornecedorController.buscarPorFoto - inicio");
        if (foto == null) {
            throw new FornecedorException("Foto não pode ser nula");
        }
        List<Fornecedor> list = dao.findByFoto(foto);
        Logger.info("FornecedorController.buscarPorFoto - sucesso");
        return list;
    }

    public List<Fornecedor> buscarPorEndereco(String endereco) {
        Logger.info("FornecedorController.buscarPorEndereco - inicio");
        if (endereco == null || endereco.isEmpty()) {
            throw new FornecedorException("Endereço não pode ser vazio");
        }
        List<Fornecedor> list = dao.findByEndereco(endereco);
        Logger.info("FornecedorController.buscarPorEndereco - sucesso");
        return list;
    }

    public List<Fornecedor> buscarPorOnline(Boolean online) {
        Logger.info("FornecedorController.buscarPorOnline - inicio");
        if (online == null) {
            throw new FornecedorException("Online não pode ser nulo");
        }
        List<Fornecedor> list = dao.findByOnline(online);
        Logger.info("FornecedorController.buscarPorOnline - sucesso");
        return list;
    }

    public List<Fornecedor> pesquisar(Fornecedor filtro) {
        Logger.info("FornecedorController.pesquisar - inicio");
        List<Fornecedor> list = dao.search(filtro);
        Logger.info("FornecedorController.pesquisar - sucesso");
        return list;
    }

    public List<Fornecedor> pesquisar(Fornecedor filtro, int page, int size) {
        Logger.info("FornecedorController.pesquisar(page) - inicio");
        List<Fornecedor> list = dao.search(filtro, page, size);
        Logger.info("FornecedorController.pesquisar(page) - sucesso");
        return list;
    }
}
