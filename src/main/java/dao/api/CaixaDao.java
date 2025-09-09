// path: src/main/java/dao/api/CaixaDao.java
package dao.api;

import java.math.BigDecimal;
import java.util.List;

import exception.CaixaException;
import model.Caixa;

public interface CaixaDao {

    void create(Caixa caixa) throws CaixaException;

    Caixa update(Caixa caixa) throws CaixaException;

    void deleteById(Integer id) throws CaixaException;

    Caixa findById(Integer id) throws CaixaException;

    List<Caixa> findAll();

    List<Caixa> findAll(int page, int size);

    List<Caixa> findByNome(String nome);

    List<Caixa> findByReservaEmergencia(BigDecimal reservaEmergencia);

    List<Caixa> findBySalarioMedio(BigDecimal salarioMedio);

    List<Caixa> findByValorTotal(BigDecimal valorTotal);

    List<Caixa> findByIdUsuario(Integer idUsuario);

    List<Caixa> search(Caixa filtro);

    List<Caixa> search(Caixa filtro, int page, int size);
}
