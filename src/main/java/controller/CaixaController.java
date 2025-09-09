// path: src/main/java/controller/CaixaController.java
package controller;

import java.math.BigDecimal;
import java.util.List;

import dao.api.CaixaDao;
import exception.CaixaException;
import infra.Logger;
import model.Caixa;

public class CaixaController {

    private final CaixaDao dao;

    public CaixaController(CaixaDao dao) {
        this.dao = dao;
    }

    public void criar(Caixa caixa) {
        Logger.info("CaixaController.criar - inicio");
        if (caixa == null) {
            throw new CaixaException("Caixa não pode ser nula");
        }
        if (caixa.getIdCaixa() == null) {
            throw new CaixaException("Id da Caixa é obrigatório");
        }
        if (caixa.getNome() == null || caixa.getNome().isEmpty()) {
            throw new CaixaException("Nome da Caixa é obrigatório");
        }
        dao.create(caixa);
        Logger.info("CaixaController.criar - sucesso");
    }

    public Caixa atualizar(Caixa caixa) {
        Logger.info("CaixaController.atualizar - inicio");
        if (caixa == null || caixa.getIdCaixa() == null) {
            throw new CaixaException("Caixa ou Id não pode ser nulo");
        }
        if (caixa.getNome() == null || caixa.getNome().isEmpty()) {
            throw new CaixaException("Nome da Caixa é obrigatório");
        }
        Caixa updated = dao.update(caixa);
        Logger.info("CaixaController.atualizar - sucesso");
        return updated;
    }

    public void remover(Integer id) {
        Logger.info("CaixaController.remover - inicio");
        if (id == null) {
            throw new CaixaException("Id da Caixa é obrigatório");
        }
        dao.deleteById(id);
        Logger.info("CaixaController.remover - sucesso");
    }

    public Caixa buscarPorId(Integer id) {
        Logger.info("CaixaController.buscarPorId - inicio");
        if (id == null) {
            throw new CaixaException("Id da Caixa é obrigatório");
        }
        Caixa c = dao.findById(id);
        Logger.info("CaixaController.buscarPorId - sucesso");
        return c;
    }

    public List<Caixa> listar() {
        Logger.info("CaixaController.listar - inicio");
        List<Caixa> list = dao.findAll();
        Logger.info("CaixaController.listar - sucesso");
        return list;
    }

    public List<Caixa> listar(int page, int size) {
        Logger.info("CaixaController.listar(page) - inicio");
        List<Caixa> list = dao.findAll(page, size);
        Logger.info("CaixaController.listar(page) - sucesso");
        return list;
    }

    public List<Caixa> buscarPorNome(String nome) {
        Logger.info("CaixaController.buscarPorNome - inicio");
        if (nome == null || nome.isEmpty()) {
            throw new CaixaException("Nome não pode ser vazio");
        }
        List<Caixa> list = dao.findByNome(nome);
        Logger.info("CaixaController.buscarPorNome - sucesso");
        return list;
    }

    public List<Caixa> buscarPorReservaEmergencia(BigDecimal reserva) {
        Logger.info("CaixaController.buscarPorReservaEmergencia - inicio");
        if (reserva == null) {
            throw new CaixaException("Reserva não pode ser nula");
        }
        List<Caixa> list = dao.findByReservaEmergencia(reserva);
        Logger.info("CaixaController.buscarPorReservaEmergencia - sucesso");
        return list;
    }

    public List<Caixa> buscarPorSalarioMedio(BigDecimal salario) {
        Logger.info("CaixaController.buscarPorSalarioMedio - inicio");
        if (salario == null) {
            throw new CaixaException("Salário não pode ser nulo");
        }
        List<Caixa> list = dao.findBySalarioMedio(salario);
        Logger.info("CaixaController.buscarPorSalarioMedio - sucesso");
        return list;
    }

    public List<Caixa> buscarPorValorTotal(BigDecimal valor) {
        Logger.info("CaixaController.buscarPorValorTotal - inicio");
        if (valor == null) {
            throw new CaixaException("Valor não pode ser nulo");
        }
        List<Caixa> list = dao.findByValorTotal(valor);
        Logger.info("CaixaController.buscarPorValorTotal - sucesso");
        return list;
    }

    public List<Caixa> buscarPorIdUsuario(Integer idUsuario) {
        Logger.info("CaixaController.buscarPorIdUsuario - inicio");
        if (idUsuario == null) {
            throw new CaixaException("Id do usuário não pode ser nulo");
        }
        List<Caixa> list = dao.findByIdUsuario(idUsuario);
        Logger.info("CaixaController.buscarPorIdUsuario - sucesso");
        return list;
    }

    public List<Caixa> pesquisar(Caixa filtro) {
        Logger.info("CaixaController.pesquisar - inicio");
        List<Caixa> list = dao.search(filtro);
        Logger.info("CaixaController.pesquisar - sucesso");
        return list;
    }

    public List<Caixa> pesquisar(Caixa filtro, int page, int size) {
        Logger.info("CaixaController.pesquisar(page) - inicio");
        List<Caixa> list = dao.search(filtro, page, size);
        Logger.info("CaixaController.pesquisar(page) - sucesso");
        return list;
    }
}
