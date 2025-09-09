// path: src/main/java/dao/api/ExercicioDao.java
package dao.api;

import java.util.List;

import exception.ExercicioException;
import model.Exercicio;

public interface ExercicioDao {

    void create(Exercicio exercicio) throws ExercicioException;

    Exercicio update(Exercicio exercicio) throws ExercicioException;

    void deleteById(Integer id) throws ExercicioException;

    Exercicio findById(Integer id) throws ExercicioException;

    List<Exercicio> findAll();

    List<Exercicio> findAll(int page, int size);

    List<Exercicio> findByNome(String nome);

    List<Exercicio> findByCargaLeve(Integer cargaLeve);

    List<Exercicio> findByCargaMedia(Integer cargaMedia);

    List<Exercicio> findByCargaMaxima(Integer cargaMaxima);

    List<Exercicio> search(Exercicio filtro);

    List<Exercicio> search(Exercicio filtro, int page, int size);
}
