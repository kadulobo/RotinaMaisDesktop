// path: src/main/java/dao/api/ObjetoDao.java
package dao.api;

import java.math.BigDecimal;
import java.util.List;

import exception.ObjetoException;
import model.Objeto;

public interface ObjetoDao {

    void create(Objeto objeto) throws ObjetoException;

    Objeto update(Objeto objeto) throws ObjetoException;

    void deleteById(Integer id) throws ObjetoException;

    Objeto findById(Integer id) throws ObjetoException;

    Objeto findWithFotoById(Integer id) throws ObjetoException;

    List<Objeto> findAll();

    List<Objeto> findAll(int page, int size);

    List<Objeto> findByNome(String nome);

    List<Objeto> findByTipo(Integer tipo);

    List<Objeto> findByValor(BigDecimal valor);

    List<Objeto> findByDescricao(String descricao);

    List<Objeto> findByFoto(byte[] foto);

    List<Objeto> search(Objeto filtro);

    List<Objeto> search(Objeto filtro, int page, int size);
}
