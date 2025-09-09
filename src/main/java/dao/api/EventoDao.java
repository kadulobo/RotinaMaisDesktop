// path: src/main/java/dao/api/EventoDao.java
package dao.api;

import java.time.LocalDate;
import java.util.List;

import exception.EventoException;
import model.Evento;

public interface EventoDao {
    void create(Evento evento) throws EventoException;

    Evento update(Evento evento) throws EventoException;

    void deleteById(Integer id) throws EventoException;

    Evento findById(Integer id) throws EventoException;

    Evento findWithBlobsById(Integer id) throws EventoException;

    List<Evento> findAll();

    List<Evento> findAll(int page, int size);

    List<Evento> findByVantagem(Boolean vantagem);

    List<Evento> findByFoto(byte[] foto);

    List<Evento> findByNome(String nome);

    List<Evento> findByDescricao(String descricao);

    List<Evento> findByDataCriacao(LocalDate dataCriacao);

    List<Evento> findByIdCategoria(Integer idCategoria);

    List<Evento> search(Evento filtro);

    List<Evento> search(Evento filtro, int page, int size);
}
