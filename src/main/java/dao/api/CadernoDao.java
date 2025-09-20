package dao.api;

import java.time.LocalDate;
import java.util.List;

import model.Caderno;

public interface CadernoDao {
    void create(Caderno caderno);
    void update(Caderno caderno);
    void deleteById(Integer id);
    Caderno findById(Integer id);
    List<Caderno> findAll();
    List<Caderno> findAll(int page, int size);
    List<Caderno> findByTitulo(String titulo);
    List<Caderno> findByData(LocalDate data);
    List<Caderno> findByIdUsuario(Integer idUsuario);
    List<Caderno> findByIdCategoria(Integer idCategoria);
    List<Caderno> search(Caderno filtro);
    List<Caderno> search(Caderno filtro, int page, int size);
}
