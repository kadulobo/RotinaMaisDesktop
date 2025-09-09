// path: src/main/java/controller/ObjetoController.java
package controller;

import java.math.BigDecimal;
import java.util.List;

import dao.api.ObjetoDao;
import exception.ObjetoException;
import infra.Logger;
import model.Objeto;

public class ObjetoController {

    private final ObjetoDao dao;

    public ObjetoController(ObjetoDao dao) {
        this.dao = dao;
    }

    public void criar(Objeto objeto) {
        Logger.info("ObjetoController.criar - inicio");
        if (objeto == null) {
            throw new ObjetoException("Objeto não pode ser nulo");
        }
        if (objeto.getIdObjeto() == null) {
            throw new ObjetoException("Id do Objeto é obrigatório");
        }
        if (objeto.getNome() == null || objeto.getNome().isEmpty()) {
            throw new ObjetoException("Nome do Objeto é obrigatório");
        }
        dao.create(objeto);
        Logger.info("ObjetoController.criar - sucesso");
    }

    public Objeto atualizar(Objeto objeto) {
        Logger.info("ObjetoController.atualizar - inicio");
        if (objeto == null || objeto.getIdObjeto() == null) {
            throw new ObjetoException("Objeto ou Id não pode ser nulo");
        }
        if (objeto.getNome() == null || objeto.getNome().isEmpty()) {
            throw new ObjetoException("Nome do Objeto é obrigatório");
        }
        Objeto updated = dao.update(objeto);
        Logger.info("ObjetoController.atualizar - sucesso");
        return updated;
    }

    public void remover(Integer id) {
        Logger.info("ObjetoController.remover - inicio");
        if (id == null) {
            throw new ObjetoException("Id do Objeto é obrigatório");
        }
        dao.deleteById(id);
        Logger.info("ObjetoController.remover - sucesso");
    }

    public Objeto buscarPorId(Integer id) {
        Logger.info("ObjetoController.buscarPorId - inicio");
        if (id == null) {
            throw new ObjetoException("Id do Objeto é obrigatório");
        }
        Objeto o = dao.findById(id);
        Logger.info("ObjetoController.buscarPorId - sucesso");
        return o;
    }

    public Objeto buscarComFotoPorId(Integer id) {
        Logger.info("ObjetoController.buscarComFotoPorId - inicio");
        if (id == null) {
            throw new ObjetoException("Id do Objeto é obrigatório");
        }
        Objeto o = dao.findWithFotoById(id);
        Logger.info("ObjetoController.buscarComFotoPorId - sucesso");
        return o;
    }

    public List<Objeto> listar() {
        Logger.info("ObjetoController.listar - inicio");
        List<Objeto> list = dao.findAll();
        Logger.info("ObjetoController.listar - sucesso");
        return list;
    }

    public List<Objeto> listar(int page, int size) {
        Logger.info("ObjetoController.listar(page) - inicio");
        List<Objeto> list = dao.findAll(page, size);
        Logger.info("ObjetoController.listar(page) - sucesso");
        return list;
    }

    public List<Objeto> buscarPorNome(String nome) {
        Logger.info("ObjetoController.buscarPorNome - inicio");
        if (nome == null || nome.isEmpty()) {
            throw new ObjetoException("Nome não pode ser vazio");
        }
        List<Objeto> list = dao.findByNome(nome);
        Logger.info("ObjetoController.buscarPorNome - sucesso");
        return list;
    }

    public List<Objeto> buscarPorTipo(Integer tipo) {
        Logger.info("ObjetoController.buscarPorTipo - inicio");
        if (tipo == null) {
            throw new ObjetoException("Tipo não pode ser nulo");
        }
        List<Objeto> list = dao.findByTipo(tipo);
        Logger.info("ObjetoController.buscarPorTipo - sucesso");
        return list;
    }

    public List<Objeto> buscarPorValor(BigDecimal valor) {
        Logger.info("ObjetoController.buscarPorValor - inicio");
        if (valor == null) {
            throw new ObjetoException("Valor não pode ser nulo");
        }
        List<Objeto> list = dao.findByValor(valor);
        Logger.info("ObjetoController.buscarPorValor - sucesso");
        return list;
    }

    public List<Objeto> buscarPorDescricao(String descricao) {
        Logger.info("ObjetoController.buscarPorDescricao - inicio");
        if (descricao == null || descricao.isEmpty()) {
            throw new ObjetoException("Descrição não pode ser vazia");
        }
        List<Objeto> list = dao.findByDescricao(descricao);
        Logger.info("ObjetoController.buscarPorDescricao - sucesso");
        return list;
    }

    public List<Objeto> buscarPorFoto(byte[] foto) {
        Logger.info("ObjetoController.buscarPorFoto - inicio");
        if (foto == null) {
            throw new ObjetoException("Foto não pode ser nula");
        }
        List<Objeto> list = dao.findByFoto(foto);
        Logger.info("ObjetoController.buscarPorFoto - sucesso");
        return list;
    }

    public List<Objeto> pesquisar(Objeto filtro) {
        Logger.info("ObjetoController.pesquisar - inicio");
        List<Objeto> list = dao.search(filtro);
        Logger.info("ObjetoController.pesquisar - sucesso");
        return list;
    }

    public List<Objeto> pesquisar(Objeto filtro, int page, int size) {
        Logger.info("ObjetoController.pesquisar(page) - inicio");
        List<Objeto> list = dao.search(filtro, page, size);
        Logger.info("ObjetoController.pesquisar(page) - sucesso");
        return list;
    }
}
