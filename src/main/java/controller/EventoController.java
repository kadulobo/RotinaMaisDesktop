// path: src/main/java/controller/EventoController.java
package controller;

import java.time.LocalDate;
import java.util.List;

import dao.api.EventoDao;
import exception.EventoException;
import infra.Logger;
import model.Evento;

public class EventoController {

    private final EventoDao dao;

    public EventoController(EventoDao dao) {
        this.dao = dao;
    }

    public void criar(Evento evento) {
        Logger.info("EventoController.criar - inicio");
        if (evento == null) {
            throw new EventoException("Evento não pode ser nulo");
        }
        if (evento.getNome() == null || evento.getNome().isEmpty()) {
            throw new EventoException("Nome do Evento é obrigatório");
        }
        dao.create(evento);
        Logger.info("EventoController.criar - sucesso");
    }

    public Evento atualizar(Evento evento) {
        Logger.info("EventoController.atualizar - inicio");
        if (evento == null || evento.getIdEvento() == null) {
            throw new EventoException("Evento ou Id não pode ser nulo");
        }
        if (evento.getNome() == null || evento.getNome().isEmpty()) {
            throw new EventoException("Nome do Evento é obrigatório");
        }
        Evento updated = dao.update(evento);
        Logger.info("EventoController.atualizar - sucesso");
        return updated;
    }

    public void remover(Integer id) {
        Logger.info("EventoController.remover - inicio");
        if (id == null) {
            throw new EventoException("Id do Evento é obrigatório");
        }
        dao.deleteById(id);
        Logger.info("EventoController.remover - sucesso");
    }

    public Evento buscarPorId(Integer id) {
        Logger.info("EventoController.buscarPorId - inicio");
        if (id == null) {
            throw new EventoException("Id do Evento é obrigatório");
        }
        Evento e = dao.findById(id);
        Logger.info("EventoController.buscarPorId - sucesso");
        return e;
    }

    public Evento buscarComFotoPorId(Integer id) {
        Logger.info("EventoController.buscarComFotoPorId - inicio");
        if (id == null) {
            throw new EventoException("Id do Evento é obrigatório");
        }
        Evento e = dao.findWithBlobsById(id);
        Logger.info("EventoController.buscarComFotoPorId - sucesso");
        return e;
    }

    public List<Evento> listar() {
        Logger.info("EventoController.listar - inicio");
        List<Evento> list = dao.findAll();
        Logger.info("EventoController.listar - sucesso");
        return list;
    }

    public List<Evento> listar(int page, int size) {
        Logger.info("EventoController.listar(page) - inicio");
        List<Evento> list = dao.findAll(page, size);
        Logger.info("EventoController.listar(page) - sucesso");
        return list;
    }

    public List<Evento> buscarPorVantagem(Boolean vantagem) {
        Logger.info("EventoController.buscarPorVantagem - inicio");
        if (vantagem == null) {
            throw new EventoException("Vantagem não pode ser nula");
        }
        List<Evento> list = dao.findByVantagem(vantagem);
        Logger.info("EventoController.buscarPorVantagem - sucesso");
        return list;
    }

    public List<Evento> buscarPorFoto(byte[] foto) {
        Logger.info("EventoController.buscarPorFoto - inicio");
        if (foto == null) {
            throw new EventoException("Foto não pode ser nula");
        }
        List<Evento> list = dao.findByFoto(foto);
        Logger.info("EventoController.buscarPorFoto - sucesso");
        return list;
    }

    public List<Evento> buscarPorNome(String nome) {
        Logger.info("EventoController.buscarPorNome - inicio");
        if (nome == null || nome.isEmpty()) {
            throw new EventoException("Nome não pode ser vazio");
        }
        List<Evento> list = dao.findByNome(nome);
        Logger.info("EventoController.buscarPorNome - sucesso");
        return list;
    }

    public List<Evento> buscarPorDescricao(String descricao) {
        Logger.info("EventoController.buscarPorDescricao - inicio");
        if (descricao == null || descricao.isEmpty()) {
            throw new EventoException("Descrição não pode ser vazia");
        }
        List<Evento> list = dao.findByDescricao(descricao);
        Logger.info("EventoController.buscarPorDescricao - sucesso");
        return list;
    }

    public List<Evento> buscarPorDataCriacao(LocalDate dataCriacao) {
        Logger.info("EventoController.buscarPorDataCriacao - inicio");
        if (dataCriacao == null) {
            throw new EventoException("Data de criação não pode ser nula");
        }
        List<Evento> list = dao.findByDataCriacao(dataCriacao);
        Logger.info("EventoController.buscarPorDataCriacao - sucesso");
        return list;
    }

    public List<Evento> buscarPorIdCategoria(Integer idCategoria) {
        Logger.info("EventoController.buscarPorIdCategoria - inicio");
        if (idCategoria == null) {
            throw new EventoException("Id da categoria não pode ser nulo");
        }
        List<Evento> list = dao.findByIdCategoria(idCategoria);
        Logger.info("EventoController.buscarPorIdCategoria - sucesso");
        return list;
    }

    public List<Evento> pesquisar(Evento filtro) {
        Logger.info("EventoController.pesquisar - inicio");
        List<Evento> list = dao.search(filtro);
        Logger.info("EventoController.pesquisar - sucesso");
        return list;
    }

    public List<Evento> pesquisar(Evento filtro, int page, int size) {
        Logger.info("EventoController.pesquisar(page) - inicio");
        List<Evento> list = dao.search(filtro, page, size);
        Logger.info("EventoController.pesquisar(page) - sucesso");
        return list;
    }
}
