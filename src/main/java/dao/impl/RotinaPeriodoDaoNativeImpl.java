package dao.impl;

import dao.api.RotinaPeriodoDao;
import exception.RotinaPeriodoException;
import infra.EntityManagerUtil;
import infra.Logger;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import model.RotinaPeriodo;

import java.util.List;

public class RotinaPeriodoDaoNativeImpl implements RotinaPeriodoDao {
    private final EntityManager em = EntityManagerUtil.getEntityManager();

    @Override
    public void create(RotinaPeriodo e) {
        Logger.info("RotinaPeriodoDaoNativeImpl.create");
        em.getTransaction().begin();
        em.persist(e);
        em.getTransaction().commit();
    }

    @Override
    public void update(RotinaPeriodo e) {
        Logger.info("RotinaPeriodoDaoNativeImpl.update");
        em.getTransaction().begin();
        em.merge(e);
        em.getTransaction().commit();
    }

    @Override
    public void deleteById(Integer id) {
        Logger.info("RotinaPeriodoDaoNativeImpl.deleteById");
        RotinaPeriodo e = em.find(RotinaPeriodo.class, id);
        if (e == null) throw new RotinaPeriodoException("RotinaPeriodo nao encontrado: id=" + id);
        em.getTransaction().begin();
        em.remove(e);
        em.getTransaction().commit();
    }

    @Override
    public RotinaPeriodo findById(Integer id) {
        String sql = "SELECT id_rotina_periodo, id_rotina, id_periodo FROM Rotina_Periodo WHERE id_rotina_periodo = :id";
        Query q = em.createNativeQuery(sql, RotinaPeriodo.class).setParameter("id", id);
        return (RotinaPeriodo) q.getSingleResult();
    }

    @Override
    public List<RotinaPeriodo> findAll() {
        String sql = "SELECT id_rotina_periodo, id_rotina, id_periodo FROM Rotina_Periodo";
        return em.createNativeQuery(sql, RotinaPeriodo.class).getResultList();
    }

    @Override
    public List<RotinaPeriodo> findAll(int page, int size) {
        String sql = "SELECT id_rotina_periodo, id_rotina, id_periodo FROM Rotina_Periodo LIMIT :size OFFSET :off";
        return em.createNativeQuery(sql, RotinaPeriodo.class)
                .setParameter("size", size)
                .setParameter("off", page * size)
                .getResultList();
    }

    @Override
    public List<RotinaPeriodo> findByIdRotina(Integer idRotina) {
        String sql = "SELECT id_rotina_periodo, id_rotina, id_periodo FROM Rotina_Periodo WHERE id_rotina = :v";
        return em.createNativeQuery(sql, RotinaPeriodo.class).setParameter("v", idRotina).getResultList();
    }

    @Override
    public List<RotinaPeriodo> findByIdPeriodo(Integer idPeriodo) {
        String sql = "SELECT id_rotina_periodo, id_rotina, id_periodo FROM Rotina_Periodo WHERE id_periodo = :v";
        return em.createNativeQuery(sql, RotinaPeriodo.class).setParameter("v", idPeriodo).getResultList();
    }

    @Override
    public List<RotinaPeriodo> search(RotinaPeriodo f) {
        return search(f, 0, Integer.MAX_VALUE);
    }

    @Override
    public List<RotinaPeriodo> search(RotinaPeriodo f, int page, int size) {
        StringBuilder sb = new StringBuilder("SELECT id_rotina_periodo, id_rotina, id_periodo FROM Rotina_Periodo WHERE 1=1");
        if (f.getIdRotina() != null) sb.append(" AND id_rotina = :idRotina");
        if (f.getIdPeriodo() != null) sb.append(" AND id_periodo = :idPeriodo");
        Query q = em.createNativeQuery(sb.toString(), RotinaPeriodo.class);
        if (f.getIdRotina() != null) q.setParameter("idRotina", f.getIdRotina());
        if (f.getIdPeriodo() != null) q.setParameter("idPeriodo", f.getIdPeriodo());
        q.setFirstResult(page * size);
        q.setMaxResults(size);
        return q.getResultList();
    }
}