// path: src/main/java/dao/api/IngredienteDao.java
package dao.api;

import java.util.List;

import exception.IngredienteException;
import model.Ingrediente;

public interface IngredienteDao {

    void create(Ingrediente ingrediente) throws IngredienteException;

    Ingrediente update(Ingrediente ingrediente) throws IngredienteException;

    void deleteById(Integer id) throws IngredienteException;

    Ingrediente findById(Integer id) throws IngredienteException;

    Ingrediente findWithFotoById(Integer id) throws IngredienteException;

    List<Ingrediente> findAll();

    List<Ingrediente> findAll(int page, int size);

    List<Ingrediente> findByFoto(byte[] foto);

    List<Ingrediente> findByNome(String nome);

    List<Ingrediente> findByDescricao(String descricao);

    List<Ingrediente> search(Ingrediente filtro);

    List<Ingrediente> search(Ingrediente filtro, int page, int size);
}
