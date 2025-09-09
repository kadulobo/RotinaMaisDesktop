package dao.impl;

import dao.api.OperacaoDao;
import exception.OperacaoException;
import infra.EntityManagerUtil;
import infra.Logger;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import model.Operacao;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;

public class OperacaoDaoNativeImpl implements OperacaoDao {
    private final EntityManager em = EntityManagerUtil.getEntityManager();

    @Override
    public void create(Operacao e) {
        Logger.info("OperacaoDaoNativeImpl.create");
        em.getTransaction().begin();
        em.persist(e);
        em.getTransaction().commit();
    }

    @Override
    public void update(Operacao e) {
        Logger.info("OperacaoDaoNativeImpl.update");
        em.getTransaction().begin();
        em.merge(e);
        em.getTransaction().commit();
    }

    @Override
    public void deleteById(Integer id) {
        Logger.info("OperacaoDaoNativeImpl.deleteById");
        Operacao e = em.find(Operacao.class, id);
        if (e == null) throw new OperacaoException("Operacao nao encontrado: id=" + id);
        em.getTransaction().begin();
        em.remove(e);
        em.getTransaction().commit();
    }

    @Override
    public Operacao findById(Integer id) {
        String sql = "SELECT id_operacao, fechamento, tempo_operacao, qtd_compra, abertura, qtd_venda, lado, preco_compra, preco_venda, preco_medio, res_intervalo, numero_operacao, res_operacao, drawdon, ganhoMax, perdaMax, tet, total, id_carteira, id_papel FROM Operacao WHERE id_operacao = :id";
        Query q = em.createNativeQuery(sql, Operacao.class).setParameter("id", id);
        return (Operacao) q.getSingleResult();
    }

    @Override
    public List<Operacao> findAll() {
        String sql = "SELECT id_operacao, fechamento, tempo_operacao, qtd_compra, abertura, qtd_venda, lado, preco_compra, preco_venda, preco_medio, res_intervalo, numero_operacao, res_operacao, drawdon, ganhoMax, perdaMax, tet, total, id_carteira, id_papel FROM Operacao";
        return em.createNativeQuery(sql, Operacao.class).getResultList();
    }

    @Override
    public List<Operacao> findAll(int page, int size) {
        String sql = "SELECT id_operacao, fechamento, tempo_operacao, qtd_compra, abertura, qtd_venda, lado, preco_compra, preco_venda, preco_medio, res_intervalo, numero_operacao, res_operacao, drawdon, ganhoMax, perdaMax, tet, total, id_carteira, id_papel FROM Operacao LIMIT :size OFFSET :off";
        return em.createNativeQuery(sql, Operacao.class)
                .setParameter("size", size)
                .setParameter("off", page * size)
                .getResultList();
    }

    @Override
    public List<Operacao> findByFechamento(BigDecimal fechamento) {
        String sql = "SELECT id_operacao, fechamento, tempo_operacao, qtd_compra, abertura, qtd_venda, lado, preco_compra, preco_venda, preco_medio, res_intervalo, numero_operacao, res_operacao, drawdon, ganhoMax, perdaMax, tet, total, id_carteira, id_papel FROM Operacao WHERE fechamento = :v";
        return em.createNativeQuery(sql, Operacao.class).setParameter("v", fechamento).getResultList();
    }

    @Override
    public List<Operacao> findByTempoOperacao(LocalTime tempoOperacao) {
        String sql = "SELECT id_operacao, fechamento, tempo_operacao, qtd_compra, abertura, qtd_venda, lado, preco_compra, preco_venda, preco_medio, res_intervalo, numero_operacao, res_operacao, drawdon, ganhoMax, perdaMax, tet, total, id_carteira, id_papel FROM Operacao WHERE tempo_operacao = :v";
        return em.createNativeQuery(sql, Operacao.class).setParameter("v", tempoOperacao).getResultList();
    }

    @Override
    public List<Operacao> findByQtdCompra(Integer qtdCompra) {
        String sql = "SELECT id_operacao, fechamento, tempo_operacao, qtd_compra, abertura, qtd_venda, lado, preco_compra, preco_venda, preco_medio, res_intervalo, numero_operacao, res_operacao, drawdon, ganhoMax, perdaMax, tet, total, id_carteira, id_papel FROM Operacao WHERE qtd_compra = :v";
        return em.createNativeQuery(sql, Operacao.class).setParameter("v", qtdCompra).getResultList();
    }

    @Override
    public List<Operacao> findByAbertura(BigDecimal abertura) {
        String sql = "SELECT id_operacao, fechamento, tempo_operacao, qtd_compra, abertura, qtd_venda, lado, preco_compra, preco_venda, preco_medio, res_intervalo, numero_operacao, res_operacao, drawdon, ganhoMax, perdaMax, tet, total, id_carteira, id_papel FROM Operacao WHERE abertura = :v";
        return em.createNativeQuery(sql, Operacao.class).setParameter("v", abertura).getResultList();
    }

    @Override
    public List<Operacao> findByQtdVenda(Integer qtdVenda) {
        String sql = "SELECT id_operacao, fechamento, tempo_operacao, qtd_compra, abertura, qtd_venda, lado, preco_compra, preco_venda, preco_medio, res_intervalo, numero_operacao, res_operacao, drawdon, ganhoMax, perdaMax, tet, total, id_carteira, id_papel FROM Operacao WHERE qtd_venda = :v";
        return em.createNativeQuery(sql, Operacao.class).setParameter("v", qtdVenda).getResultList();
    }

    @Override
    public List<Operacao> findByLado(String lado) {
        String sql = "SELECT id_operacao, fechamento, tempo_operacao, qtd_compra, abertura, qtd_venda, lado, preco_compra, preco_venda, preco_medio, res_intervalo, numero_operacao, res_operacao, drawdon, ganhoMax, perdaMax, tet, total, id_carteira, id_papel FROM Operacao WHERE lado = :v";
        return em.createNativeQuery(sql, Operacao.class).setParameter("v", lado).getResultList();
    }

    @Override
    public List<Operacao> findByPrecoCompra(BigDecimal precoCompra) {
        String sql = "SELECT id_operacao, fechamento, tempo_operacao, qtd_compra, abertura, qtd_venda, lado, preco_compra, preco_venda, preco_medio, res_intervalo, numero_operacao, res_operacao, drawdon, ganhoMax, perdaMax, tet, total, id_carteira, id_papel FROM Operacao WHERE preco_compra = :v";
        return em.createNativeQuery(sql, Operacao.class).setParameter("v", precoCompra).getResultList();
    }

    @Override
    public List<Operacao> findByPrecoVenda(BigDecimal precoVenda) {
        String sql = "SELECT id_operacao, fechamento, tempo_operacao, qtd_compra, abertura, qtd_venda, lado, preco_compra, preco_venda, preco_medio, res_intervalo, numero_operacao, res_operacao, drawdon, ganhoMax, perdaMax, tet, total, id_carteira, id_papel FROM Operacao WHERE preco_venda = :v";
        return em.createNativeQuery(sql, Operacao.class).setParameter("v", precoVenda).getResultList();
    }

    @Override
    public List<Operacao> findByPrecoMedio(BigDecimal precoMedio) {
        String sql = "SELECT id_operacao, fechamento, tempo_operacao, qtd_compra, abertura, qtd_venda, lado, preco_compra, preco_venda, preco_medio, res_intervalo, numero_operacao, res_operacao, drawdon, ganhoMax, perdaMax, tet, total, id_carteira, id_papel FROM Operacao WHERE preco_medio = :v";
        return em.createNativeQuery(sql, Operacao.class).setParameter("v", precoMedio).getResultList();
    }

    @Override
    public List<Operacao> findByResIntervalo(String resIntervalo) {
        String sql = "SELECT id_operacao, fechamento, tempo_operacao, qtd_compra, abertura, qtd_venda, lado, preco_compra, preco_venda, preco_medio, res_intervalo, numero_operacao, res_operacao, drawdon, ganhoMax, perdaMax, tet, total, id_carteira, id_papel FROM Operacao WHERE res_intervalo = :v";
        return em.createNativeQuery(sql, Operacao.class).setParameter("v", resIntervalo).getResultList();
    }

    @Override
    public List<Operacao> findByNumeroOperacao(BigDecimal numeroOperacao) {
        String sql = "SELECT id_operacao, fechamento, tempo_operacao, qtd_compra, abertura, qtd_venda, lado, preco_compra, preco_venda, preco_medio, res_intervalo, numero_operacao, res_operacao, drawdon, ganhoMax, perdaMax, tet, total, id_carteira, id_papel FROM Operacao WHERE numero_operacao = :v";
        return em.createNativeQuery(sql, Operacao.class).setParameter("v", numeroOperacao).getResultList();
    }

    @Override
    public List<Operacao> findByResOperacao(String resOperacao) {
        String sql = "SELECT id_operacao, fechamento, tempo_operacao, qtd_compra, abertura, qtd_venda, lado, preco_compra, preco_venda, preco_medio, res_intervalo, numero_operacao, res_operacao, drawdon, ganhoMax, perdaMax, tet, total, id_carteira, id_papel FROM Operacao WHERE res_operacao = :v";
        return em.createNativeQuery(sql, Operacao.class).setParameter("v", resOperacao).getResultList();
    }

    @Override
    public List<Operacao> findByDrawdon(BigDecimal drawdon) {
        String sql = "SELECT id_operacao, fechamento, tempo_operacao, qtd_compra, abertura, qtd_venda, lado, preco_compra, preco_venda, preco_medio, res_intervalo, numero_operacao, res_operacao, drawdon, ganhoMax, perdaMax, tet, total, id_carteira, id_papel FROM Operacao WHERE drawdon = :v";
        return em.createNativeQuery(sql, Operacao.class).setParameter("v", drawdon).getResultList();
    }

    @Override
    public List<Operacao> findByGanhoMax(BigDecimal ganhoMax) {
        String sql = "SELECT id_operacao, fechamento, tempo_operacao, qtd_compra, abertura, qtd_venda, lado, preco_compra, preco_venda, preco_medio, res_intervalo, numero_operacao, res_operacao, drawdon, ganhoMax, perdaMax, tet, total, id_carteira, id_papel FROM Operacao WHERE ganhoMax = :v";
        return em.createNativeQuery(sql, Operacao.class).setParameter("v", ganhoMax).getResultList();
    }

    @Override
    public List<Operacao> findByPerdaMax(BigDecimal perdaMax) {
        String sql = "SELECT id_operacao, fechamento, tempo_operacao, qtd_compra, abertura, qtd_venda, lado, preco_compra, preco_venda, preco_medio, res_intervalo, numero_operacao, res_operacao, drawdon, ganhoMax, perdaMax, tet, total, id_carteira, id_papel FROM Operacao WHERE perdaMax = :v";
        return em.createNativeQuery(sql, Operacao.class).setParameter("v", perdaMax).getResultList();
    }

    @Override
    public List<Operacao> findByTet(String tet) {
        String sql = "SELECT id_operacao, fechamento, tempo_operacao, qtd_compra, abertura, qtd_venda, lado, preco_compra, preco_venda, preco_medio, res_intervalo, numero_operacao, res_operacao, drawdon, ganhoMax, perdaMax, tet, total, id_carteira, id_papel FROM Operacao WHERE tet = :v";
        return em.createNativeQuery(sql, Operacao.class).setParameter("v", tet).getResultList();
    }

    @Override
    public List<Operacao> findByTotal(BigDecimal total) {
        String sql = "SELECT id_operacao, fechamento, tempo_operacao, qtd_compra, abertura, qtd_venda, lado, preco_compra, preco_venda, preco_medio, res_intervalo, numero_operacao, res_operacao, drawdon, ganhoMax, perdaMax, tet, total, id_carteira, id_papel FROM Operacao WHERE total = :v";
        return em.createNativeQuery(sql, Operacao.class).setParameter("v", total).getResultList();
    }

    @Override
    public List<Operacao> findByIdCarteira(Integer idCarteira) {
        String sql = "SELECT id_operacao, fechamento, tempo_operacao, qtd_compra, abertura, qtd_venda, lado, preco_compra, preco_venda, preco_medio, res_intervalo, numero_operacao, res_operacao, drawdon, ganhoMax, perdaMax, tet, total, id_carteira, id_papel FROM Operacao WHERE id_carteira = :v";
        return em.createNativeQuery(sql, Operacao.class).setParameter("v", idCarteira).getResultList();
    }

    @Override
    public List<Operacao> findByIdPapel(Integer idPapel) {
        String sql = "SELECT id_operacao, fechamento, tempo_operacao, qtd_compra, abertura, qtd_venda, lado, preco_compra, preco_venda, preco_medio, res_intervalo, numero_operacao, res_operacao, drawdon, ganhoMax, perdaMax, tet, total, id_carteira, id_papel FROM Operacao WHERE id_papel = :v";
        return em.createNativeQuery(sql, Operacao.class).setParameter("v", idPapel).getResultList();
    }

    @Override
    public List<Operacao> search(Operacao f) {
        return search(f, 0, Integer.MAX_VALUE);
    }

    @Override
    public List<Operacao> search(Operacao f, int page, int size) {
        StringBuilder sb = new StringBuilder("SELECT id_operacao, fechamento, tempo_operacao, qtd_compra, abertura, qtd_venda, lado, preco_compra, preco_venda, preco_medio, res_intervalo, numero_operacao, res_operacao, drawdon, ganhoMax, perdaMax, tet, total, id_carteira, id_papel FROM Operacao WHERE 1=1");
        if (f.getFechamento() != null) sb.append(" AND fechamento = :fechamento");
        if (f.getTempoOperacao() != null) sb.append(" AND tempo_operacao = :tempoOperacao");
        if (f.getQtdCompra() != null) sb.append(" AND qtd_compra = :qtdCompra");
        if (f.getAbertura() != null) sb.append(" AND abertura = :abertura");
        if (f.getQtdVenda() != null) sb.append(" AND qtd_venda = :qtdVenda");
        if (f.getLado() != null && !f.getLado().isEmpty()) sb.append(" AND lado = :lado");
        if (f.getPrecoCompra() != null) sb.append(" AND preco_compra = :precoCompra");
        if (f.getPrecoVenda() != null) sb.append(" AND preco_venda = :precoVenda");
        if (f.getPrecoMedio() != null) sb.append(" AND preco_medio = :precoMedio");
        if (f.getResIntervalo() != null && !f.getResIntervalo().isEmpty()) sb.append(" AND res_intervalo = :resIntervalo");
        if (f.getNumeroOperacao() != null) sb.append(" AND numero_operacao = :numeroOperacao");
        if (f.getResOperacao() != null && !f.getResOperacao().isEmpty()) sb.append(" AND res_operacao = :resOperacao");
        if (f.getDrawdon() != null) sb.append(" AND drawdon = :drawdon");
        if (f.getGanhoMax() != null) sb.append(" AND ganhoMax = :ganhoMax");
        if (f.getPerdaMax() != null) sb.append(" AND perdaMax = :perdaMax");
        if (f.getTet() != null && !f.getTet().isEmpty()) sb.append(" AND tet = :tet");
        if (f.getTotal() != null) sb.append(" AND total = :total");
        if (f.getIdCarteira() != null) sb.append(" AND id_carteira = :idCarteira");
        if (f.getIdPapel() != null) sb.append(" AND id_papel = :idPapel");
        Query q = em.createNativeQuery(sb.toString(), Operacao.class);
        if (f.getFechamento() != null) q.setParameter("fechamento", f.getFechamento());
        if (f.getTempoOperacao() != null) q.setParameter("tempoOperacao", f.getTempoOperacao());
        if (f.getQtdCompra() != null) q.setParameter("qtdCompra", f.getQtdCompra());
        if (f.getAbertura() != null) q.setParameter("abertura", f.getAbertura());
        if (f.getQtdVenda() != null) q.setParameter("qtdVenda", f.getQtdVenda());
        if (f.getLado() != null && !f.getLado().isEmpty()) q.setParameter("lado", f.getLado());
        if (f.getPrecoCompra() != null) q.setParameter("precoCompra", f.getPrecoCompra());
        if (f.getPrecoVenda() != null) q.setParameter("precoVenda", f.getPrecoVenda());
        if (f.getPrecoMedio() != null) q.setParameter("precoMedio", f.getPrecoMedio());
        if (f.getResIntervalo() != null && !f.getResIntervalo().isEmpty()) q.setParameter("resIntervalo", f.getResIntervalo());
        if (f.getNumeroOperacao() != null) q.setParameter("numeroOperacao", f.getNumeroOperacao());
        if (f.getResOperacao() != null && !f.getResOperacao().isEmpty()) q.setParameter("resOperacao", f.getResOperacao());
        if (f.getDrawdon() != null) q.setParameter("drawdon", f.getDrawdon());
        if (f.getGanhoMax() != null) q.setParameter("ganhoMax", f.getGanhoMax());
        if (f.getPerdaMax() != null) q.setParameter("perdaMax", f.getPerdaMax());
        if (f.getTet() != null && !f.getTet().isEmpty()) q.setParameter("tet", f.getTet());
        if (f.getTotal() != null) q.setParameter("total", f.getTotal());
        if (f.getIdCarteira() != null) q.setParameter("idCarteira", f.getIdCarteira());
        if (f.getIdPapel() != null) q.setParameter("idPapel", f.getIdPapel());
        q.setFirstResult(page * size);
        q.setMaxResults(size);
        return q.getResultList();
    }
}