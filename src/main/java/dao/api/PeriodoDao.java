// path: src/main/java/dao/api/PeriodoDao.java
package dao.api;

import java.util.List;

import exception.PeriodoException;
import model.Periodo;

public interface PeriodoDao {

    void create(Periodo periodo) throws PeriodoException;

    Periodo update(Periodo periodo) throws PeriodoException;

    void deleteById(Integer id) throws PeriodoException;

    Periodo findById(Integer id) throws PeriodoException;

    List<Periodo> findAll();

    List<Periodo> findAll(int page, int size);

    List<Periodo> findByAno(Integer ano);

    List<Periodo> findByMes(Integer mes);

    List<Periodo> search(Periodo filtro);

    List<Periodo> search(Periodo filtro, int page, int size);
}
