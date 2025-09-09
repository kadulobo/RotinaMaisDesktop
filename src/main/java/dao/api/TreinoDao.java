// path: src/main/java/dao/api/TreinoDao.java
package dao.api;

import java.util.List;

import exception.TreinoException;
import model.Treino;

public interface TreinoDao {

    void create(Treino treino) throws TreinoException;

    Treino update(Treino treino) throws TreinoException;

    void deleteById(Integer id) throws TreinoException;

    Treino findById(Integer id) throws TreinoException;

    List<Treino> findAll();

    List<Treino> findAll(int page, int size);

    List<Treino> findByNome(String nome);

    List<Treino> findByClasse(String classe);

    List<Treino> findByIdRotina(Integer idRotina);

    List<Treino> search(Treino filtro);

    List<Treino> search(Treino filtro, int page, int size);
}
