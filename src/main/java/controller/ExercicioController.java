// path: src/main/java/controller/ExercicioController.java
package controller;

import java.util.List;

import dao.api.ExercicioDao;
import exception.ExercicioException;
import infra.Logger;
import model.Exercicio;

public class ExercicioController {

    private final ExercicioDao dao;

    public ExercicioController(ExercicioDao dao) {
        this.dao = dao;
    }

    public void criar(Exercicio exercicio) {
        Logger.info("ExercicioController.criar - inicio");
        if (exercicio == null) {
            throw new ExercicioException("Exercicio não pode ser nulo");
        }
        if (exercicio.getNome() == null || exercicio.getNome().isEmpty()) {
            throw new ExercicioException("Nome do Exercicio é obrigatório");
        }
        dao.create(exercicio);
        Logger.info("ExercicioController.criar - sucesso");
    }

    public Exercicio atualizar(Exercicio exercicio) {
        Logger.info("ExercicioController.atualizar - inicio");
        if (exercicio == null || exercicio.getIdExercicio() == null) {
            throw new ExercicioException("Exercicio ou Id não pode ser nulo");
        }
        if (exercicio.getNome() == null || exercicio.getNome().isEmpty()) {
            throw new ExercicioException("Nome do Exercicio é obrigatório");
        }
        Exercicio updated = dao.update(exercicio);
        Logger.info("ExercicioController.atualizar - sucesso");
        return updated;
    }

    public void remover(Integer id) {
        Logger.info("ExercicioController.remover - inicio");
        if (id == null) {
            throw new ExercicioException("Id do Exercicio é obrigatório");
        }
        dao.deleteById(id);
        Logger.info("ExercicioController.remover - sucesso");
    }

    public Exercicio buscarPorId(Integer id) {
        Logger.info("ExercicioController.buscarPorId - inicio");
        if (id == null) {
            throw new ExercicioException("Id do Exercicio é obrigatório");
        }
        Exercicio e = dao.findById(id);
        Logger.info("ExercicioController.buscarPorId - sucesso");
        return e;
    }

    public List<Exercicio> listar() {
        Logger.info("ExercicioController.listar - inicio");
        List<Exercicio> list = dao.findAll();
        Logger.info("ExercicioController.listar - sucesso");
        return list;
    }

    public List<Exercicio> listar(int page, int size) {
        Logger.info("ExercicioController.listar(page) - inicio");
        List<Exercicio> list = dao.findAll(page, size);
        Logger.info("ExercicioController.listar(page) - sucesso");
        return list;
    }

    public List<Exercicio> buscarPorNome(String nome) {
        Logger.info("ExercicioController.buscarPorNome - inicio");
        if (nome == null || nome.isEmpty()) {
            throw new ExercicioException("Nome não pode ser vazio");
        }
        List<Exercicio> list = dao.findByNome(nome);
        Logger.info("ExercicioController.buscarPorNome - sucesso");
        return list;
    }

    public List<Exercicio> buscarPorCargaLeve(Integer cargaLeve) {
        Logger.info("ExercicioController.buscarPorCargaLeve - inicio");
        if (cargaLeve == null) {
            throw new ExercicioException("Carga leve não pode ser nula");
        }
        List<Exercicio> list = dao.findByCargaLeve(cargaLeve);
        Logger.info("ExercicioController.buscarPorCargaLeve - sucesso");
        return list;
    }

    public List<Exercicio> buscarPorCargaMedia(Integer cargaMedia) {
        Logger.info("ExercicioController.buscarPorCargaMedia - inicio");
        if (cargaMedia == null) {
            throw new ExercicioException("Carga média não pode ser nula");
        }
        List<Exercicio> list = dao.findByCargaMedia(cargaMedia);
        Logger.info("ExercicioController.buscarPorCargaMedia - sucesso");
        return list;
    }

    public List<Exercicio> buscarPorCargaMaxima(Integer cargaMaxima) {
        Logger.info("ExercicioController.buscarPorCargaMaxima - inicio");
        if (cargaMaxima == null) {
            throw new ExercicioException("Carga máxima não pode ser nula");
        }
        List<Exercicio> list = dao.findByCargaMaxima(cargaMaxima);
        Logger.info("ExercicioController.buscarPorCargaMaxima - sucesso");
        return list;
    }

    public List<Exercicio> pesquisar(Exercicio filtro) {
        Logger.info("ExercicioController.pesquisar - inicio");
        List<Exercicio> list = dao.search(filtro);
        Logger.info("ExercicioController.pesquisar - sucesso");
        return list;
    }

    public List<Exercicio> pesquisar(Exercicio filtro, int page, int size) {
        Logger.info("ExercicioController.pesquisar(page) - inicio");
        List<Exercicio> list = dao.search(filtro, page, size);
        Logger.info("ExercicioController.pesquisar(page) - sucesso");
        return list;
    }
}
