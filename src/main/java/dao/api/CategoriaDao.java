// path: src/main/java/dao/api/CategoriaDao.java
package dao.api;

import java.time.LocalDate;
import java.util.List;

import exception.CategoriaException;
import model.Categoria;

public interface CategoriaDao {
    void create(Categoria categoria) throws CategoriaException;

    Categoria update(Categoria categoria) throws CategoriaException;

    void deleteById(Integer id) throws CategoriaException;

    Categoria findById(Integer id) throws CategoriaException;

    Categoria findWithBlobsById(Integer id) throws CategoriaException;

    List<Categoria> findAll();

    List<Categoria> findAll(int page, int size);

    List<Categoria> findByNome(String nome);

    List<Categoria> findByDescricao(String descricao);

    List<Categoria> findByDataCriacao(LocalDate dataCriacao);

    List<Categoria> findByFoto(byte[] foto);

    List<Categoria> search(Categoria filtro);

    List<Categoria> search(Categoria filtro, int page, int size);
}
