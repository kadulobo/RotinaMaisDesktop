// path: src/main/java/dao/api/MovimentacaoDao.java
package dao.api;

import java.math.BigDecimal;
import java.util.List;

import exception.MovimentacaoException;
import model.Movimentacao;

public interface MovimentacaoDao {

    void create(Movimentacao movimentacao) throws MovimentacaoException;

    Movimentacao update(Movimentacao movimentacao) throws MovimentacaoException;

    void deleteById(Integer id) throws MovimentacaoException;

    Movimentacao findById(Integer id) throws MovimentacaoException;

    List<Movimentacao> findAll();

    List<Movimentacao> findAll(int page, int size);

    List<Movimentacao> findByDesconto(BigDecimal desconto);

    List<Movimentacao> findByVantagem(BigDecimal vantagem);

    List<Movimentacao> findByLiquido(BigDecimal liquido);

    List<Movimentacao> findByTipo(Integer tipo);

    List<Movimentacao> findByStatus(Integer status);

    List<Movimentacao> findByPonto(Integer ponto);

    List<Movimentacao> findByIdUsuario(Integer idUsuario);

    List<Movimentacao> findByIdCaixa(Integer idCaixa);

    List<Movimentacao> findByIdPeriodo(Integer idPeriodo);

    List<Movimentacao> search(Movimentacao filtro);

    List<Movimentacao> search(Movimentacao filtro, int page, int size);
}
