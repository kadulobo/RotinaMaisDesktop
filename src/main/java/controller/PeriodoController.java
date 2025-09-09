// path: src/main/java/controller/PeriodoController.java
package controller;

import java.util.List;

import dao.api.PeriodoDao;
import exception.PeriodoException;
import infra.Logger;
import model.Periodo;

public class PeriodoController {

    private final PeriodoDao dao;

    public PeriodoController(PeriodoDao dao) {
        this.dao = dao;
    }

    public void criar(Periodo periodo) {
        Logger.info("PeriodoController.criar - inicio");
        if (periodo == null) {
            throw new PeriodoException("Periodo não pode ser nulo");
        }
        if (periodo.getIdPeriodo() == null) {
            throw new PeriodoException("Id do Periodo é obrigatório");
        }
        if (periodo.getAno() == null) {
            throw new PeriodoException("Ano do Periodo é obrigatório");
        }
        if (periodo.getMes() == null) {
            throw new PeriodoException("Mês do Periodo é obrigatório");
        }
        dao.create(periodo);
        Logger.info("PeriodoController.criar - sucesso");
    }

    public Periodo atualizar(Periodo periodo) {
        Logger.info("PeriodoController.atualizar - inicio");
        if (periodo == null || periodo.getIdPeriodo() == null) {
            throw new PeriodoException("Periodo ou Id não pode ser nulo");
        }
        if (periodo.getAno() == null) {
            throw new PeriodoException("Ano do Periodo é obrigatório");
        }
        if (periodo.getMes() == null) {
            throw new PeriodoException("Mês do Periodo é obrigatório");
        }
        Periodo updated = dao.update(periodo);
        Logger.info("PeriodoController.atualizar - sucesso");
        return updated;
    }

    public void remover(Integer id) {
        Logger.info("PeriodoController.remover - inicio");
        if (id == null) {
            throw new PeriodoException("Id do Periodo é obrigatório");
        }
        dao.deleteById(id);
        Logger.info("PeriodoController.remover - sucesso");
    }

    public Periodo buscarPorId(Integer id) {
        Logger.info("PeriodoController.buscarPorId - inicio");
        if (id == null) {
            throw new PeriodoException("Id do Periodo é obrigatório");
        }
        Periodo p = dao.findById(id);
        Logger.info("PeriodoController.buscarPorId - sucesso");
        return p;
    }

    public List<Periodo> listar() {
        Logger.info("PeriodoController.listar - inicio");
        List<Periodo> list = dao.findAll();
        Logger.info("PeriodoController.listar - sucesso");
        return list;
    }

    public List<Periodo> listar(int page, int size) {
        Logger.info("PeriodoController.listar(page) - inicio");
        List<Periodo> list = dao.findAll(page, size);
        Logger.info("PeriodoController.listar(page) - sucesso");
        return list;
    }

    public List<Periodo> buscarPorAno(Integer ano) {
        Logger.info("PeriodoController.buscarPorAno - inicio");
        if (ano == null) {
            throw new PeriodoException("Ano não pode ser nulo");
        }
        List<Periodo> list = dao.findByAno(ano);
        Logger.info("PeriodoController.buscarPorAno - sucesso");
        return list;
    }

    public List<Periodo> buscarPorMes(Integer mes) {
        Logger.info("PeriodoController.buscarPorMes - inicio");
        if (mes == null) {
            throw new PeriodoException("Mês não pode ser nulo");
        }
        List<Periodo> list = dao.findByMes(mes);
        Logger.info("PeriodoController.buscarPorMes - sucesso");
        return list;
    }

    public List<Periodo> pesquisar(Periodo filtro) {
        Logger.info("PeriodoController.pesquisar - inicio");
        List<Periodo> list = dao.search(filtro);
        Logger.info("PeriodoController.pesquisar - sucesso");
        return list;
    }

    public List<Periodo> pesquisar(Periodo filtro, int page, int size) {
        Logger.info("PeriodoController.pesquisar(page) - inicio");
        List<Periodo> list = dao.search(filtro, page, size);
        Logger.info("PeriodoController.pesquisar(page) - sucesso");
        return list;
    }
}
