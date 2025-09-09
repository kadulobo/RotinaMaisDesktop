// path: src/main/java/dao/api/LancamentoDao.java
package dao.api;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import exception.LancamentoException;
import model.Lancamento;

public interface LancamentoDao {

    void create(Lancamento lancamento) throws LancamentoException;

    Lancamento update(Lancamento lancamento) throws LancamentoException;

    void deleteById(Integer id) throws LancamentoException;

    Lancamento findById(Integer id) throws LancamentoException;

    List<Lancamento> findAll();

    List<Lancamento> findAll(int page, int size);

    List<Lancamento> findByValor(BigDecimal valor);

    List<Lancamento> findByFixo(Boolean fixo);

    List<Lancamento> findByDataPagamento(LocalDate dataPagamento);

    List<Lancamento> findByStatus(Integer status);

    List<Lancamento> findByIdMovimentacao(Integer idMovimentacao);

    List<Lancamento> findByIdEvento(Integer idEvento);

    List<Lancamento> search(Lancamento filtro);

    List<Lancamento> search(Lancamento filtro, int page, int size);
}
