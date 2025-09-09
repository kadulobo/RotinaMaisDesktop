// path: src/main/java/controller/RotinaController.java
package controller;

import java.time.LocalDate;
import java.util.List;

import dao.api.RotinaDao;
import exception.RotinaException;
import infra.Logger;
import model.Rotina;

public class RotinaController {

    private final RotinaDao dao;

    public RotinaController(RotinaDao dao) {
        this.dao = dao;
    }

    public void criar(Rotina rotina) {
        Logger.info("RotinaController.criar - inicio");
        if (rotina == null) {
            throw new RotinaException("Rotina não pode ser nula");
        }
        if (rotina.getIdRotina() == null) {
            throw new RotinaException("Id da Rotina é obrigatório");
        }
        if (rotina.getNome() == null || rotina.getNome().isEmpty()) {
            throw new RotinaException("Nome da Rotina é obrigatório");
        }
        dao.create(rotina);
        Logger.info("RotinaController.criar - sucesso");
    }

    public Rotina atualizar(Rotina rotina) {
        Logger.info("RotinaController.atualizar - inicio");
        if (rotina == null || rotina.getIdRotina() == null) {
            throw new RotinaException("Rotina ou Id não pode ser nulo");
        }
        if (rotina.getNome() == null || rotina.getNome().isEmpty()) {
            throw new RotinaException("Nome da Rotina é obrigatório");
        }
        Rotina updated = dao.update(rotina);
        Logger.info("RotinaController.atualizar - sucesso");
        return updated;
    }

    public void remover(Integer id) {
        Logger.info("RotinaController.remover - inicio");
        if (id == null) {
            throw new RotinaException("Id da Rotina é obrigatório");
        }
        dao.deleteById(id);
        Logger.info("RotinaController.remover - sucesso");
    }

    public Rotina buscarPorId(Integer id) {
        Logger.info("RotinaController.buscarPorId - inicio");
        if (id == null) {
            throw new RotinaException("Id da Rotina é obrigatório");
        }
        Rotina r = dao.findById(id);
        Logger.info("RotinaController.buscarPorId - sucesso");
        return r;
    }

    public List<Rotina> listar() {
        Logger.info("RotinaController.listar - inicio");
        List<Rotina> list = dao.findAll();
        Logger.info("RotinaController.listar - sucesso");
        return list;
    }

    public List<Rotina> listar(int page, int size) {
        Logger.info("RotinaController.listar(page) - inicio");
        List<Rotina> list = dao.findAll(page, size);
        Logger.info("RotinaController.listar(page) - sucesso");
        return list;
    }

    public List<Rotina> buscarPorNome(String nome) {
        Logger.info("RotinaController.buscarPorNome - inicio");
        if (nome == null || nome.isEmpty()) {
            throw new RotinaException("Nome não pode ser vazio");
        }
        List<Rotina> list = dao.findByNome(nome);
        Logger.info("RotinaController.buscarPorNome - sucesso");
        return list;
    }

    public List<Rotina> buscarPorInicio(LocalDate inicio) {
        Logger.info("RotinaController.buscarPorInicio - inicio");
        if (inicio == null) {
            throw new RotinaException("Inicio não pode ser nulo");
        }
        List<Rotina> list = dao.findByInicio(inicio);
        Logger.info("RotinaController.buscarPorInicio - sucesso");
        return list;
    }

    public List<Rotina> buscarPorFim(LocalDate fim) {
        Logger.info("RotinaController.buscarPorFim - inicio");
        if (fim == null) {
            throw new RotinaException("Fim não pode ser nulo");
        }
        List<Rotina> list = dao.findByFim(fim);
        Logger.info("RotinaController.buscarPorFim - sucesso");
        return list;
    }

    public List<Rotina> buscarPorDescricao(String descricao) {
        Logger.info("RotinaController.buscarPorDescricao - inicio");
        if (descricao == null || descricao.isEmpty()) {
            throw new RotinaException("Descrição não pode ser vazia");
        }
        List<Rotina> list = dao.findByDescricao(descricao);
        Logger.info("RotinaController.buscarPorDescricao - sucesso");
        return list;
    }

    public List<Rotina> buscarPorStatus(Integer status) {
        Logger.info("RotinaController.buscarPorStatus - inicio");
        if (status == null) {
            throw new RotinaException("Status não pode ser nulo");
        }
        List<Rotina> list = dao.findByStatus(status);
        Logger.info("RotinaController.buscarPorStatus - sucesso");
        return list;
    }

    public List<Rotina> buscarPorPonto(Integer ponto) {
        Logger.info("RotinaController.buscarPorPonto - inicio");
        if (ponto == null) {
            throw new RotinaException("Ponto não pode ser nulo");
        }
        List<Rotina> list = dao.findByPonto(ponto);
        Logger.info("RotinaController.buscarPorPonto - sucesso");
        return list;
    }

    public List<Rotina> buscarPorIdUsuario(Integer idUsuario) {
        Logger.info("RotinaController.buscarPorIdUsuario - inicio");
        if (idUsuario == null) {
            throw new RotinaException("Id do usuário não pode ser nulo");
        }
        List<Rotina> list = dao.findByIdUsuario(idUsuario);
        Logger.info("RotinaController.buscarPorIdUsuario - sucesso");
        return list;
    }

    public List<Rotina> pesquisar(Rotina filtro) {
        Logger.info("RotinaController.pesquisar - inicio");
        List<Rotina> list = dao.search(filtro);
        Logger.info("RotinaController.pesquisar - sucesso");
        return list;
    }

    public List<Rotina> pesquisar(Rotina filtro, int page, int size) {
        Logger.info("RotinaController.pesquisar(page) - inicio");
        List<Rotina> list = dao.search(filtro, page, size);
        Logger.info("RotinaController.pesquisar(page) - sucesso");
        return list;
    }
}
