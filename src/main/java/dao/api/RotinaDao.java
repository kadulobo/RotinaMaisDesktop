// path: src/main/java/dao/api/RotinaDao.java
package dao.api;

import java.time.LocalDate;
import java.util.List;

import exception.RotinaException;
import model.Rotina;

public interface RotinaDao {

    void create(Rotina rotina) throws RotinaException;

    Rotina update(Rotina rotina) throws RotinaException;

    void deleteById(Integer id) throws RotinaException;

    Rotina findById(Integer id) throws RotinaException;

    List<Rotina> findAll();

    List<Rotina> findAll(int page, int size);

    List<Rotina> findByNome(String nome);

    List<Rotina> findByInicio(LocalDate inicio);

    List<Rotina> findByFim(LocalDate fim);

    List<Rotina> findByDescricao(String descricao);

    List<Rotina> findByStatus(Integer status);

    List<Rotina> findByPonto(Integer ponto);

    List<Rotina> findByIdUsuario(Integer idUsuario);

    List<Rotina> search(Rotina filtro);

    List<Rotina> search(Rotina filtro, int page, int size);
}
