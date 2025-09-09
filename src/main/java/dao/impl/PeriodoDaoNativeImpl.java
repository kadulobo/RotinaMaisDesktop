// path: src/main/java/dao/impl/PeriodoDaoNativeImpl.java
package dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dao.api.PeriodoDao;
import exception.PeriodoException;
import infra.EntityManagerUtil;
import infra.Logger;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import model.Periodo;

public class PeriodoDaoNativeImpl implements PeriodoDao {

    @Override
    public void create(Periodo periodo) throws PeriodoException {
        Logger.info("PeriodoDaoNativeImpl.create - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            String sql = "INSERT INTO Periodo (ano, mes) VALUES (:ano, :mes)";
            Query query = em.createNativeQuery(sql);
            query.setParameter("ano", periodo.getAno());
            query.setParameter("mes", periodo.getMes());
            query.executeUpdate();
            em.getTransaction().commit();
            Logger.info("PeriodoDaoNativeImpl.create - sucesso");
        } catch (Exception e) {
            em.getTransaction().rollback();
            Logger.error("PeriodoDaoNativeImpl.create - erro", e);
            throw new PeriodoException("Erro ao criar Periodo", e);
        } finally {
            em.close();
        }
    }

    @Override
    public Periodo update(Periodo periodo) throws PeriodoException {
        Logger.info("PeriodoDaoNativeImpl.update - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            String sql = "UPDATE Periodo SET ano=:ano, mes=:mes WHERE id_periodo=:id";
            Query query = em.createNativeQuery(sql);
            query.setParameter("ano", periodo.getAno());
            query.setParameter("mes", periodo.getMes());
            query.setParameter("id", periodo.getIdPeriodo());
            int updated = query.executeUpdate();
            if (updated == 0) {
                throw new PeriodoException("Periodo não encontrado: id=" + periodo.getIdPeriodo());
            }
            em.getTransaction().commit();
            Logger.info("PeriodoDaoNativeImpl.update - sucesso");
            return findById(periodo.getIdPeriodo());
        } catch (PeriodoException e) {
            em.getTransaction().rollback();
            Logger.error("PeriodoDaoNativeImpl.update - erro", e);
            throw e;
        } catch (Exception e) {
            em.getTransaction().rollback();
            Logger.error("PeriodoDaoNativeImpl.update - erro", e);
            throw new PeriodoException("Erro ao atualizar Periodo", e);
        } finally {
            em.close();
        }
    }

    @Override
    public void deleteById(Integer id) throws PeriodoException {
        Logger.info("PeriodoDaoNativeImpl.deleteById - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            String sql = "DELETE FROM Periodo WHERE id_periodo=:id";
            Query query = em.createNativeQuery(sql);
            query.setParameter("id", id);
            int deleted = query.executeUpdate();
            if (deleted == 0) {
                throw new PeriodoException("Periodo não encontrado: id=" + id);
            }
            em.getTransaction().commit();
            Logger.info("PeriodoDaoNativeImpl.deleteById - sucesso");
        } catch (PeriodoException e) {
            em.getTransaction().rollback();
            Logger.error("PeriodoDaoNativeImpl.deleteById - erro", e);
            throw e;
        } catch (Exception e) {
            em.getTransaction().rollback();
            Logger.error("PeriodoDaoNativeImpl.deleteById - erro", e);
            throw new PeriodoException("Erro ao deletar Periodo", e);
        } finally {
            em.close();
        }
    }

    @Override
    public Periodo findById(Integer id) throws PeriodoException {
        Logger.info("PeriodoDaoNativeImpl.findById - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_periodo, ano, mes FROM Periodo WHERE id_periodo=:id";
            Query query = em.createNativeQuery(sql, Periodo.class);
            query.setParameter("id", id);
            Periodo p = (Periodo) query.getSingleResult();
            Logger.info("PeriodoDaoNativeImpl.findById - sucesso");
            return p;
        } catch (Exception e) {
            Logger.error("PeriodoDaoNativeImpl.findById - erro", e);
            throw new PeriodoException("Periodo não encontrado: id=" + id, e);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Periodo> findAll() {
        Logger.info("PeriodoDaoNativeImpl.findAll - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_periodo, ano, mes FROM Periodo";
            Query query = em.createNativeQuery(sql, Periodo.class);
            List<Periodo> list = query.getResultList();
            Logger.info("PeriodoDaoNativeImpl.findAll - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Periodo> findAll(int page, int size) {
        Logger.info("PeriodoDaoNativeImpl.findAll(page) - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_periodo, ano, mes FROM Periodo LIMIT :limit OFFSET :offset";
            Query query = em.createNativeQuery(sql, Periodo.class);
            query.setParameter("limit", size);
            query.setParameter("offset", page * size);
            List<Periodo> list = query.getResultList();
            Logger.info("PeriodoDaoNativeImpl.findAll(page) - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Periodo> findByAno(Integer ano) {
        Logger.info("PeriodoDaoNativeImpl.findByAno - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_periodo, ano, mes FROM Periodo WHERE ano=:ano";
            Query query = em.createNativeQuery(sql, Periodo.class);
            query.setParameter("ano", ano);
            List<Periodo> list = query.getResultList();
            Logger.info("PeriodoDaoNativeImpl.findByAno - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Periodo> findByMes(Integer mes) {
        Logger.info("PeriodoDaoNativeImpl.findByMes - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_periodo, ano, mes FROM Periodo WHERE mes=:mes";
            Query query = em.createNativeQuery(sql, Periodo.class);
            query.setParameter("mes", mes);
            List<Periodo> list = query.getResultList();
            Logger.info("PeriodoDaoNativeImpl.findByMes - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Periodo> search(Periodo filtro) {
        return search(filtro, -1, -1);
    }

    @Override
    public List<Periodo> search(Periodo filtro, int page, int size) {
        Logger.info("PeriodoDaoNativeImpl.search - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            StringBuilder sb = new StringBuilder("SELECT id_periodo, ano, mes FROM Periodo WHERE 1=1");
            Map<String, Object> params = new HashMap<>();
            if (filtro.getAno() != null) {
                sb.append(" AND ano=:ano");
                params.put("ano", filtro.getAno());
            }
            if (filtro.getMes() != null) {
                sb.append(" AND mes=:mes");
                params.put("mes", filtro.getMes());
            }
            if (page >= 0 && size > 0) {
                sb.append(" LIMIT :limit OFFSET :offset");
            }
            Query query = em.createNativeQuery(sb.toString(), Periodo.class);
            for (Map.Entry<String, Object> e : params.entrySet()) {
                query.setParameter(e.getKey(), e.getValue());
            }
            if (page >= 0 && size > 0) {
                query.setParameter("limit", size);
                query.setParameter("offset", page * size);
            }
            List<Periodo> list = query.getResultList();
            Logger.info("PeriodoDaoNativeImpl.search - sucesso");
            return list;
        } finally {
            em.close();
        }
    }
}
