package dao.api;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;

import model.Operacao;

public interface OperacaoDao {
    void create(Operacao e);
    void update(Operacao e);
    void deleteById(Integer id);
    Operacao findById(Integer id);
    List<Operacao> findAll();
    List<Operacao> findAll(int page, int size);
    List<Operacao> findByFechamento(BigDecimal fechamento);
    List<Operacao> findByTempoOperacao(LocalTime tempoOperacao);
    List<Operacao> findByQtdCompra(Integer qtdCompra);
    List<Operacao> findByAbertura(BigDecimal abertura);
    List<Operacao> findByQtdVenda(Integer qtdVenda);
    List<Operacao> findByLado(String lado);
    List<Operacao> findByPrecoCompra(BigDecimal precoCompra);
    List<Operacao> findByPrecoVenda(BigDecimal precoVenda);
    List<Operacao> findByPrecoMedio(BigDecimal precoMedio);
    List<Operacao> findByResIntervalo(String resIntervalo);
    List<Operacao> findByNumeroOperacao(BigDecimal numeroOperacao);
    List<Operacao> findByResOperacao(String resOperacao);
    List<Operacao> findByDrawdon(BigDecimal drawdon);
    List<Operacao> findByGanhoMax(BigDecimal ganhoMax);
    List<Operacao> findByPerdaMax(BigDecimal perdaMax);
    List<Operacao> findByTet(String tet);
    List<Operacao> findByTotal(BigDecimal total);
    List<Operacao> findByIdCarteira(Integer idCarteira);
    List<Operacao> findByIdPapel(Integer idPapel);
    List<Operacao> search(Operacao f);
    List<Operacao> search(Operacao f, int page, int size);
}