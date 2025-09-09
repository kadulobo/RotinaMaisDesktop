// path: src/main/java/dao/impl/MonitoramentoDaoNativeImpl.java
package dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dao.api.MonitoramentoDao;
import exception.MonitoramentoException;
import infra.EntityManagerUtil;
import infra.Logger;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import model.Monitoramento;
import org.hibernate.query.NativeQuery;
import org.hibernate.type.StandardBasicTypes;

public class MonitoramentoDaoNativeImpl implements MonitoramentoDao {

    @Override
    public void create(Monitoramento monitoramento) throws MonitoramentoException {
        Logger.info("MonitoramentoDaoNativeImpl.create - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            String sql = "INSERT INTO Monitoramento (status, nome, descricao, foto, id_periodo) " +
                    "VALUES (:status, :nome, :descricao, :foto, CAST(:idPeriodo AS INTEGER))";
            NativeQuery<?> query = (NativeQuery<?>) em.createNativeQuery(sql);
            query.setParameter("status", monitoramento.getStatus());
            query.setParameter("nome", monitoramento.getNome());
            query.setParameter("descricao", monitoramento.getDescricao());
            query.setParameter("foto", monitoramento.getFoto());
            Integer idPeriodo = monitoramento.getIdPeriodo();
            query.setParameter("idPeriodo", idPeriodo, StandardBasicTypes.INTEGER);
            query.executeUpdate();
            em.getTransaction().commit();
            Logger.info("MonitoramentoDaoNativeImpl.create - sucesso");
        } catch (Exception e) {
            em.getTransaction().rollback();
            Logger.error("MonitoramentoDaoNativeImpl.create - erro", e);
            throw new MonitoramentoException("Erro ao criar Monitoramento", e);
        } finally {
            em.close();
        }
    }

    @Override
    public Monitoramento update(Monitoramento monitoramento) throws MonitoramentoException {
        Logger.info("MonitoramentoDaoNativeImpl.update - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            String sql = "UPDATE Monitoramento SET status=:status, nome=:nome, descricao=:descricao, foto=:foto, id_periodo=CAST(:idPeriodo AS INTEGER) WHERE id_monitoramento=:id";
            NativeQuery<?> query = (NativeQuery<?>) em.createNativeQuery(sql);
            query.setParameter("status", monitoramento.getStatus());
            query.setParameter("nome", monitoramento.getNome());
            query.setParameter("descricao", monitoramento.getDescricao());
            query.setParameter("foto", monitoramento.getFoto());
            Integer idPeriodo = monitoramento.getIdPeriodo();
            query.setParameter("idPeriodo", idPeriodo, StandardBasicTypes.INTEGER);
            query.setParameter("id", monitoramento.getIdMonitoramento());
            int updated = query.executeUpdate();
            if (updated == 0) {
                throw new MonitoramentoException("Monitoramento não encontrado: id=" + monitoramento.getIdMonitoramento());
            }
            em.getTransaction().commit();
            Logger.info("MonitoramentoDaoNativeImpl.update - sucesso");
            return findById(monitoramento.getIdMonitoramento());
        } catch (MonitoramentoException e) {
            em.getTransaction().rollback();
            Logger.error("MonitoramentoDaoNativeImpl.update - erro", e);
            throw e;
        } catch (Exception e) {
            em.getTransaction().rollback();
            Logger.error("MonitoramentoDaoNativeImpl.update - erro", e);
            throw new MonitoramentoException("Erro ao atualizar Monitoramento", e);
        } finally {
            em.close();
        }
    }

    @Override
    public void deleteById(Integer id) throws MonitoramentoException {
        Logger.info("MonitoramentoDaoNativeImpl.deleteById - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            String sql = "DELETE FROM Monitoramento WHERE id_monitoramento=:id";
            Query query = em.createNativeQuery(sql);
            query.setParameter("id", id);
            int deleted = query.executeUpdate();
            if (deleted == 0) {
                throw new MonitoramentoException("Monitoramento não encontrado: id=" + id);
            }
            em.getTransaction().commit();
            Logger.info("MonitoramentoDaoNativeImpl.deleteById - sucesso");
        } catch (MonitoramentoException e) {
            em.getTransaction().rollback();
            Logger.error("MonitoramentoDaoNativeImpl.deleteById - erro", e);
            throw e;
        } catch (Exception e) {
            em.getTransaction().rollback();
            Logger.error("MonitoramentoDaoNativeImpl.deleteById - erro", e);
            throw new MonitoramentoException("Erro ao deletar Monitoramento", e);
        } finally {
            em.close();
        }
    }

    @Override
    public Monitoramento findById(Integer id) throws MonitoramentoException {
        Logger.info("MonitoramentoDaoNativeImpl.findById - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_monitoramento, status, nome, descricao, id_periodo FROM Monitoramento WHERE id_monitoramento=:id";
            Query query = em.createNativeQuery(sql, Monitoramento.class);
            query.setParameter("id", id);
            Monitoramento m = (Monitoramento) query.getSingleResult();
            Logger.info("MonitoramentoDaoNativeImpl.findById - sucesso");
            return m;
        } catch (Exception e) {
            Logger.error("MonitoramentoDaoNativeImpl.findById - erro", e);
            throw new MonitoramentoException("Monitoramento não encontrado: id=" + id, e);
        } finally {
            em.close();
        }
    }

    @Override
    public Monitoramento findWithFotoById(Integer id) throws MonitoramentoException {
        Logger.info("MonitoramentoDaoNativeImpl.findWithFotoById - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_monitoramento, status, nome, descricao, foto, id_periodo FROM Monitoramento WHERE id_monitoramento=:id";
            Query query = em.createNativeQuery(sql, Monitoramento.class);
            query.setParameter("id", id);
            Monitoramento m = (Monitoramento) query.getSingleResult();
            Logger.info("MonitoramentoDaoNativeImpl.findWithFotoById - sucesso");
            return m;
        } catch (Exception e) {
            Logger.error("MonitoramentoDaoNativeImpl.findWithFotoById - erro", e);
            throw new MonitoramentoException("Monitoramento não encontrado: id=" + id, e);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Monitoramento> findAll() {
        Logger.info("MonitoramentoDaoNativeImpl.findAll - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_monitoramento, status, nome, descricao, id_periodo FROM Monitoramento";
            Query query = em.createNativeQuery(sql, Monitoramento.class);
            List<Monitoramento> list = query.getResultList();
            Logger.info("MonitoramentoDaoNativeImpl.findAll - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Monitoramento> findAll(int page, int size) {
        Logger.info("MonitoramentoDaoNativeImpl.findAll(page) - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_monitoramento, status, nome, descricao, id_periodo FROM Monitoramento LIMIT :limit OFFSET :offset";
            Query query = em.createNativeQuery(sql, Monitoramento.class);
            query.setParameter("limit", size);
            query.setParameter("offset", page * size);
            List<Monitoramento> list = query.getResultList();
            Logger.info("MonitoramentoDaoNativeImpl.findAll(page) - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Monitoramento> findByStatus(Integer status) {
        Logger.info("MonitoramentoDaoNativeImpl.findByStatus - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_monitoramento, status, nome, descricao, id_periodo FROM Monitoramento WHERE status=:status";
            Query query = em.createNativeQuery(sql, Monitoramento.class);
            query.setParameter("status", status);
            List<Monitoramento> list = query.getResultList();
            Logger.info("MonitoramentoDaoNativeImpl.findByStatus - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Monitoramento> findByNome(String nome) {
        Logger.info("MonitoramentoDaoNativeImpl.findByNome - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_monitoramento, status, nome, descricao, id_periodo FROM Monitoramento WHERE nome=:nome";
            Query query = em.createNativeQuery(sql, Monitoramento.class);
            query.setParameter("nome", nome);
            List<Monitoramento> list = query.getResultList();
            Logger.info("MonitoramentoDaoNativeImpl.findByNome - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Monitoramento> findByDescricao(String descricao) {
        Logger.info("MonitoramentoDaoNativeImpl.findByDescricao - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_monitoramento, status, nome, descricao, id_periodo FROM Monitoramento WHERE descricao=:descricao";
            Query query = em.createNativeQuery(sql, Monitoramento.class);
            query.setParameter("descricao", descricao);
            List<Monitoramento> list = query.getResultList();
            Logger.info("MonitoramentoDaoNativeImpl.findByDescricao - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Monitoramento> findByFoto(byte[] foto) {
        Logger.info("MonitoramentoDaoNativeImpl.findByFoto - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_monitoramento, status, nome, descricao, foto, id_periodo FROM Monitoramento WHERE foto=:foto";
            Query query = em.createNativeQuery(sql, Monitoramento.class);
            query.setParameter("foto", foto);
            List<Monitoramento> list = query.getResultList();
            Logger.info("MonitoramentoDaoNativeImpl.findByFoto - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Monitoramento> findByIdPeriodo(Integer idPeriodo) {
        Logger.info("MonitoramentoDaoNativeImpl.findByIdPeriodo - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_monitoramento, status, nome, descricao, id_periodo FROM Monitoramento WHERE id_periodo=:idPeriodo";
            Query query = em.createNativeQuery(sql, Monitoramento.class);
            query.setParameter("idPeriodo", idPeriodo);
            List<Monitoramento> list = query.getResultList();
            Logger.info("MonitoramentoDaoNativeImpl.findByIdPeriodo - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Monitoramento> search(Monitoramento filtro) {
        return search(filtro, -1, -1);
    }

    @Override
    public List<Monitoramento> search(Monitoramento filtro, int page, int size) {
        Logger.info("MonitoramentoDaoNativeImpl.search - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            StringBuilder sb = new StringBuilder("SELECT id_monitoramento, status, nome, descricao, id_periodo FROM Monitoramento WHERE 1=1");
            Map<String, Object> params = new HashMap<>();
            if (filtro.getStatus() != null) {
                sb.append(" AND status=:status");
                params.put("status", filtro.getStatus());
            }
            if (filtro.getNome() != null && !filtro.getNome().isEmpty()) {
                sb.append(" AND nome=:nome");
                params.put("nome", filtro.getNome());
            }
            if (filtro.getDescricao() != null && !filtro.getDescricao().isEmpty()) {
                sb.append(" AND descricao=:descricao");
                params.put("descricao", filtro.getDescricao());
            }
            if (filtro.getFoto() != null) {
                sb.append(" AND foto=:foto");
                params.put("foto", filtro.getFoto());
            }
            if (filtro.getIdPeriodo() != null) {
                sb.append(" AND id_periodo=:idPeriodo");
                params.put("idPeriodo", filtro.getIdPeriodo());
            }
            if (page >= 0 && size > 0) {
                sb.append(" LIMIT :limit OFFSET :offset");
            }
            Query query = em.createNativeQuery(sb.toString(), Monitoramento.class);
            for (Map.Entry<String, Object> e : params.entrySet()) {
                query.setParameter(e.getKey(), e.getValue());
            }
            if (page >= 0 && size > 0) {
                query.setParameter("limit", size);
                query.setParameter("offset", page * size);
            }
            List<Monitoramento> list = query.getResultList();
            Logger.info("MonitoramentoDaoNativeImpl.search - sucesso");
            return list;
        } finally {
            em.close();
        }
    }
}
