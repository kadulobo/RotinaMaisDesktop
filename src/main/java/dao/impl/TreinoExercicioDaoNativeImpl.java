package dao.impl;

import dao.api.TreinoExercicioDao;
import exception.TreinoExercicioException;
import infra.EntityManagerUtil;
import infra.Logger;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import model.TreinoExercicio;

import java.util.List;

public class TreinoExercicioDaoNativeImpl implements TreinoExercicioDao {
    private final EntityManager em = EntityManagerUtil.getEntityManager();

    @Override
    public void create(TreinoExercicio e) {
        Logger.info("TreinoExercicioDaoNativeImpl.create");
        em.getTransaction().begin();
        em.persist(e);
        em.getTransaction().commit();
    }

    @Override
    public void update(TreinoExercicio e) {
        Logger.info("TreinoExercicioDaoNativeImpl.update");
        em.getTransaction().begin();
        em.merge(e);
        em.getTransaction().commit();
    }

    @Override
    public void deleteById(Integer id) {
        Logger.info("TreinoExercicioDaoNativeImpl.deleteById");
        TreinoExercicio e = em.find(TreinoExercicio.class, id);
        if (e == null) throw new TreinoExercicioException("TreinoExercicio nao encontrado: id=" + id);
        em.getTransaction().begin();
        em.remove(e);
        em.getTransaction().commit();
    }

    @Override
    public TreinoExercicio findById(Integer id) {
        String sql = "SELECT {select_cols} FROM {table} WHERE {e['fields'][0][2]} = :id";
        Query q = em.createNativeQuery(sql, {cname}.class).setParameter("id", id);
        return ({cname}) q.getSingleResult();
    }

    @Override
    public List<TreinoExercicio> findAll() {
        String sql = "SELECT {select_cols} FROM {table}";
        return em.createNativeQuery(sql, {cname}.class).getResultList();
    }

    @Override
    public List<TreinoExercicio> findAll(int page, int size) {
        String sql = "SELECT {select_cols} FROM {table} LIMIT :size OFFSET :off";
        return em.createNativeQuery(sql, {cname}.class).setParameter("size", size).setParameter("off", page * size).getResultList();
    }

    @Override
    public List<TreinoExercicio> findByQtdRepeticao(Integer qtdRepeticao) {
        String sql = "SELECT id_treino_exercicio, qtd_repeticao, tempo_descanso, ordem, feito, id_exercicio, id_treino FROM Treino_Exercicio WHERE qtd_repeticao = :v";
        return em.createNativeQuery(sql, TreinoExercicio.class).setParameter("v", qtdRepeticao).getResultList();
    }

    @Override
    public List<TreinoExercicio> findByTempoDescanso(String tempoDescanso) {
        String sql = "SELECT id_treino_exercicio, qtd_repeticao, tempo_descanso, ordem, feito, id_exercicio, id_treino FROM Treino_Exercicio WHERE tempo_descanso = :v";
        return em.createNativeQuery(sql, TreinoExercicio.class).setParameter("v", tempoDescanso).getResultList();
    }

    @Override
    public List<TreinoExercicio> findByOrdem(Integer ordem) {
        String sql = "SELECT id_treino_exercicio, qtd_repeticao, tempo_descanso, ordem, feito, id_exercicio, id_treino FROM Treino_Exercicio WHERE ordem = :v";
        return em.createNativeQuery(sql, TreinoExercicio.class).setParameter("v", ordem).getResultList();
    }

    @Override
    public List<TreinoExercicio> findByFeito(Boolean feito) {
        String sql = "SELECT id_treino_exercicio, qtd_repeticao, tempo_descanso, ordem, feito, id_exercicio, id_treino FROM Treino_Exercicio WHERE feito = :v";
        return em.createNativeQuery(sql, TreinoExercicio.class).setParameter("v", feito).getResultList();
    }

    @Override
    public List<TreinoExercicio> findByIdExercicio(Integer idExercicio) {
        String sql = "SELECT id_treino_exercicio, qtd_repeticao, tempo_descanso, ordem, feito, id_exercicio, id_treino FROM Treino_Exercicio WHERE id_exercicio = :v";
        return em.createNativeQuery(sql, TreinoExercicio.class).setParameter("v", idExercicio).getResultList();
    }

    @Override
    public List<TreinoExercicio> findByIdTreino(Integer idTreino) {
        String sql = "SELECT id_treino_exercicio, qtd_repeticao, tempo_descanso, ordem, feito, id_exercicio, id_treino FROM Treino_Exercicio WHERE id_treino = :v";
        return em.createNativeQuery(sql, TreinoExercicio.class).setParameter("v", idTreino).getResultList();
    }

    @Override
    public List<TreinoExercicio> search(TreinoExercicio f) {
        return search(f, 0, Integer.MAX_VALUE);
    }

    @Override
    public List<TreinoExercicio> search(TreinoExercicio f, int page, int size) {
        StringBuilder sb = new StringBuilder("SELECT id_treino_exercicio, qtd_repeticao, tempo_descanso, ordem, feito, id_exercicio, id_treino FROM Treino_Exercicio WHERE 1=1");
        if (f.getQtdRepeticao() != null) sb.append(" AND qtd_repeticao = :qtdRepeticao");
        if (f.getTempoDescanso() != null && !f.getTempoDescanso().isEmpty()) sb.append(" AND tempo_descanso = :tempoDescanso");
        if (f.getOrdem() != null) sb.append(" AND ordem = :ordem");
        if (f.getFeito() != null) sb.append(" AND feito = :feito");
        if (f.getIdExercicio() != null) sb.append(" AND id_exercicio = :idExercicio");
        if (f.getIdTreino() != null) sb.append(" AND id_treino = :idTreino");
        Query q = em.createNativeQuery(sb.toString(), TreinoExercicio.class);
        if (f.getQtdRepeticao() != null) q.setParameter("qtdRepeticao", f.getQtdRepeticao());
        if (f.getTempoDescanso() != null && !f.getTempoDescanso().isEmpty()) q.setParameter("tempoDescanso", f.getTempoDescanso());
        if (f.getOrdem() != null) q.setParameter("ordem", f.getOrdem());
        if (f.getFeito() != null) q.setParameter("feito", f.getFeito());
        if (f.getIdExercicio() != null) q.setParameter("idExercicio", f.getIdExercicio());
        if (f.getIdTreino() != null) q.setParameter("idTreino", f.getIdTreino());
        q.setFirstResult(page * size);
        q.setMaxResults(size);
        return q.getResultList();
    }
}