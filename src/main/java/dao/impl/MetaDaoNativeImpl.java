package dao.impl;

import dao.api.MetaDao;
import exception.MetaException;
import infra.EntityManagerUtil;
import infra.Logger;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import model.Meta;

import java.util.List;

public class MetaDaoNativeImpl implements MetaDao {
    private final EntityManager em = EntityManagerUtil.getEntityManager();

    @Override
    public void create(Meta meta) {
        Logger.info("MetaDao.create");
        em.getTransaction().begin();
        meta.setIdMeta(null);
        em.persist(meta);
        em.getTransaction().commit();
    }

    @Override
    public void update(Meta meta) {
        Logger.info("MetaDao.update" );
        em.getTransaction().begin();
        em.merge(meta);
        em.getTransaction().commit();
    }

    @Override
    public void deleteById(Integer id) {
        Logger.info("MetaDao.deleteById" );
        Meta m = em.find(Meta.class, id);
        if (m == null) throw new MetaException("Meta nao encontrada: id=" + id);
        em.getTransaction().begin();
        em.remove(m);
        em.getTransaction().commit();
    }

    @Override
    public Meta findById(Integer id) {
        String sql = "SELECT id_meta, ponto_minimo, ponto_medio, ponto_maximo, status, NULL as foto, id_periodo FROM Meta WHERE id_meta = :id";
        Query q = em.createNativeQuery(sql, Meta.class).setParameter("id", id);
        return (Meta) q.getSingleResult();
    }

    @Override
    public Meta findWithFotoById(Integer id) {
        String sql = "SELECT * FROM Meta WHERE id_meta = :id";
        Query q = em.createNativeQuery(sql, Meta.class).setParameter("id", id);
        return (Meta) q.getSingleResult();
    }

    @Override
    public List<Meta> findAll() {
        String sql = "SELECT id_meta, ponto_minimo, ponto_medio, ponto_maximo, status, NULL as foto, id_periodo FROM Meta";
        return em.createNativeQuery(sql, Meta.class).getResultList();
    }

    @Override
    public List<Meta> findAll(int page, int size) {
        String sql = "SELECT id_meta, ponto_minimo, ponto_medio, ponto_maximo, status, NULL as foto, id_periodo FROM Meta LIMIT :size OFFSET :off";
        return em.createNativeQuery(sql, Meta.class)
                .setParameter("size", size)
                .setParameter("off", page * size)
                .getResultList();
    }

    @Override
    public List<Meta> findByPontoMinimo(Integer pontoMinimo) {
        String sql = "SELECT id_meta, ponto_minimo, ponto_medio, ponto_maximo, status, NULL as foto, id_periodo FROM Meta WHERE ponto_minimo = :v";
        return em.createNativeQuery(sql, Meta.class).setParameter("v", pontoMinimo).getResultList();
    }

    @Override
    public List<Meta> findByPontoMedio(Integer pontoMedio) {
        String sql = "SELECT id_meta, ponto_minimo, ponto_medio, ponto_maximo, status, NULL as foto, id_periodo FROM Meta WHERE ponto_medio = :v";
        return em.createNativeQuery(sql, Meta.class).setParameter("v", pontoMedio).getResultList();
    }

    @Override
    public List<Meta> findByPontoMaximo(Integer pontoMaximo) {
        String sql = "SELECT id_meta, ponto_minimo, ponto_medio, ponto_maximo, status, NULL as foto, id_periodo FROM Meta WHERE ponto_maximo = :v";
        return em.createNativeQuery(sql, Meta.class).setParameter("v", pontoMaximo).getResultList();
    }

    @Override
    public List<Meta> findByStatus(Integer status) {
        String sql = "SELECT id_meta, ponto_minimo, ponto_medio, ponto_maximo, status, NULL as foto, id_periodo FROM Meta WHERE status = :v";
        return em.createNativeQuery(sql, Meta.class).setParameter("v", status).getResultList();
    }

    @Override
    public List<Meta> findByIdPeriodo(Integer idPeriodo) {
        String sql = "SELECT id_meta, ponto_minimo, ponto_medio, ponto_maximo, status, NULL as foto, id_periodo FROM Meta WHERE id_periodo = :v";
        return em.createNativeQuery(sql, Meta.class).setParameter("v", idPeriodo).getResultList();
    }

    @Override
    public List<Meta> search(Meta f) {
        return search(f, 0, Integer.MAX_VALUE);
    }

    @Override
    public List<Meta> search(Meta f, int page, int size) {
        StringBuilder sb = new StringBuilder("SELECT id_meta, ponto_minimo, ponto_medio, ponto_maximo, status, NULL as foto, id_periodo FROM Meta WHERE 1=1");
        if (f.getPontoMinimo() != null) sb.append(" AND ponto_minimo = :pontoMinimo");
        if (f.getPontoMedio() != null) sb.append(" AND ponto_medio = :pontoMedio");
        if (f.getPontoMaximo() != null) sb.append(" AND ponto_maximo = :pontoMaximo");
        if (f.getStatus() != null) sb.append(" AND status = :status");
        if (f.getIdPeriodo() != null) sb.append(" AND id_periodo = :idPeriodo");
        Query q = em.createNativeQuery(sb.toString(), Meta.class);
        if (f.getPontoMinimo() != null) q.setParameter("pontoMinimo", f.getPontoMinimo());
        if (f.getPontoMedio() != null) q.setParameter("pontoMedio", f.getPontoMedio());
        if (f.getPontoMaximo() != null) q.setParameter("pontoMaximo", f.getPontoMaximo());
        if (f.getStatus() != null) q.setParameter("status", f.getStatus());
        if (f.getIdPeriodo() != null) q.setParameter("idPeriodo", f.getIdPeriodo());
        q.setFirstResult(page * size);
        q.setMaxResults(size);
        return q.getResultList();
    }
}
