// path: src/main/java/dao/impl/RotinaDaoNativeImpl.java
package dao.impl;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dao.api.RotinaDao;
import exception.RotinaException;
import infra.EntityManagerUtil;
import infra.Logger;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import model.Rotina;
import org.hibernate.query.NativeQuery;
import org.hibernate.type.StandardBasicTypes;

public class RotinaDaoNativeImpl implements RotinaDao {

    @Override
    public void create(Rotina rotina) throws RotinaException {
        Logger.info("RotinaDaoNativeImpl.create - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            String sql = "INSERT INTO Rotina (nome, inicio, fim, descricao, status, ponto, id_usuario) " +
                    "VALUES (:nome, :inicio, :fim, :descricao, :status, :ponto, CAST(:idUsuario AS INTEGER))";
            NativeQuery<?> query = (NativeQuery<?>) em.createNativeQuery(sql);
            query.setParameter("nome", rotina.getNome());
            query.setParameter("inicio", rotina.getInicio());
            query.setParameter("fim", rotina.getFim());
            query.setParameter("descricao", rotina.getDescricao());
            query.setParameter("status", rotina.getStatus());
            query.setParameter("ponto", rotina.getPonto());
            Integer idUsuario = rotina.getIdUsuario();
            query.setParameter("idUsuario", idUsuario, StandardBasicTypes.INTEGER);
            query.executeUpdate();
            em.getTransaction().commit();
            Logger.info("RotinaDaoNativeImpl.create - sucesso");
        } catch (Exception e) {
            em.getTransaction().rollback();
            Logger.error("RotinaDaoNativeImpl.create - erro", e);
            throw new RotinaException("Erro ao criar Rotina", e);
        } finally {
            em.close();
        }
    }

    @Override
    public Rotina update(Rotina rotina) throws RotinaException {
        Logger.info("RotinaDaoNativeImpl.update - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            String sql = "UPDATE Rotina SET nome=:nome, inicio=:inicio, fim=:fim, descricao=:descricao, status=:status, ponto=:ponto, id_usuario=CAST(:idUsuario AS INTEGER) WHERE id_rotina=:id";
            NativeQuery<?> query = (NativeQuery<?>) em.createNativeQuery(sql);
            query.setParameter("nome", rotina.getNome());
            query.setParameter("inicio", rotina.getInicio());
            query.setParameter("fim", rotina.getFim());
            query.setParameter("descricao", rotina.getDescricao());
            query.setParameter("status", rotina.getStatus());
            query.setParameter("ponto", rotina.getPonto());
            Integer idUsuario = rotina.getIdUsuario();
            query.setParameter("idUsuario", idUsuario, StandardBasicTypes.INTEGER);
            query.setParameter("id", rotina.getIdRotina());
            int updated = query.executeUpdate();
            if (updated == 0) {
                throw new RotinaException("Rotina não encontrada: id=" + rotina.getIdRotina());
            }
            em.getTransaction().commit();
            Logger.info("RotinaDaoNativeImpl.update - sucesso");
            return findById(rotina.getIdRotina());
        } catch (RotinaException e) {
            em.getTransaction().rollback();
            Logger.error("RotinaDaoNativeImpl.update - erro", e);
            throw e;
        } catch (Exception e) {
            em.getTransaction().rollback();
            Logger.error("RotinaDaoNativeImpl.update - erro", e);
            throw new RotinaException("Erro ao atualizar Rotina", e);
        } finally {
            em.close();
        }
    }

    @Override
    public void deleteById(Integer id) throws RotinaException {
        Logger.info("RotinaDaoNativeImpl.deleteById - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            String sql = "DELETE FROM Rotina WHERE id_rotina=:id";
            Query query = em.createNativeQuery(sql);
            query.setParameter("id", id);
            int deleted = query.executeUpdate();
            if (deleted == 0) {
                throw new RotinaException("Rotina não encontrada: id=" + id);
            }
            em.getTransaction().commit();
            Logger.info("RotinaDaoNativeImpl.deleteById - sucesso");
        } catch (RotinaException e) {
            em.getTransaction().rollback();
            Logger.error("RotinaDaoNativeImpl.deleteById - erro", e);
            throw e;
        } catch (Exception e) {
            em.getTransaction().rollback();
            Logger.error("RotinaDaoNativeImpl.deleteById - erro", e);
            throw new RotinaException("Erro ao deletar Rotina", e);
        } finally {
            em.close();
        }
    }

    @Override
    public Rotina findById(Integer id) throws RotinaException {
        Logger.info("RotinaDaoNativeImpl.findById - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_rotina, nome, inicio, fim, descricao, status, ponto, id_usuario FROM Rotina WHERE id_rotina=:id";
            Query query = em.createNativeQuery(sql, Rotina.class);
            query.setParameter("id", id);
            Rotina r = (Rotina) query.getSingleResult();
            Logger.info("RotinaDaoNativeImpl.findById - sucesso");
            return r;
        } catch (Exception e) {
            Logger.error("RotinaDaoNativeImpl.findById - erro", e);
            throw new RotinaException("Rotina não encontrada: id=" + id, e);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Rotina> findAll() {
        Logger.info("RotinaDaoNativeImpl.findAll - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_rotina, nome, inicio, fim, descricao, status, ponto, id_usuario FROM Rotina";
            Query query = em.createNativeQuery(sql, Rotina.class);
            List<Rotina> list = query.getResultList();
            Logger.info("RotinaDaoNativeImpl.findAll - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Rotina> findAll(int page, int size) {
        Logger.info("RotinaDaoNativeImpl.findAll(page) - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_rotina, nome, inicio, fim, descricao, status, ponto, id_usuario FROM Rotina LIMIT :limit OFFSET :offset";
            Query query = em.createNativeQuery(sql, Rotina.class);
            query.setParameter("limit", size);
            query.setParameter("offset", page * size);
            List<Rotina> list = query.getResultList();
            Logger.info("RotinaDaoNativeImpl.findAll(page) - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Rotina> findByNome(String nome) {
        Logger.info("RotinaDaoNativeImpl.findByNome - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_rotina, nome, inicio, fim, descricao, status, ponto, id_usuario FROM Rotina WHERE nome=:nome";
            Query query = em.createNativeQuery(sql, Rotina.class);
            query.setParameter("nome", nome);
            List<Rotina> list = query.getResultList();
            Logger.info("RotinaDaoNativeImpl.findByNome - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Rotina> findByInicio(LocalDate inicio) {
        Logger.info("RotinaDaoNativeImpl.findByInicio - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_rotina, nome, inicio, fim, descricao, status, ponto, id_usuario FROM Rotina WHERE inicio=:inicio";
            Query query = em.createNativeQuery(sql, Rotina.class);
            query.setParameter("inicio", inicio);
            List<Rotina> list = query.getResultList();
            Logger.info("RotinaDaoNativeImpl.findByInicio - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Rotina> findByFim(LocalDate fim) {
        Logger.info("RotinaDaoNativeImpl.findByFim - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_rotina, nome, inicio, fim, descricao, status, ponto, id_usuario FROM Rotina WHERE fim=:fim";
            Query query = em.createNativeQuery(sql, Rotina.class);
            query.setParameter("fim", fim);
            List<Rotina> list = query.getResultList();
            Logger.info("RotinaDaoNativeImpl.findByFim - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Rotina> findByDescricao(String descricao) {
        Logger.info("RotinaDaoNativeImpl.findByDescricao - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_rotina, nome, inicio, fim, descricao, status, ponto, id_usuario FROM Rotina WHERE descricao=:descricao";
            Query query = em.createNativeQuery(sql, Rotina.class);
            query.setParameter("descricao", descricao);
            List<Rotina> list = query.getResultList();
            Logger.info("RotinaDaoNativeImpl.findByDescricao - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Rotina> findByStatus(Integer status) {
        Logger.info("RotinaDaoNativeImpl.findByStatus - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_rotina, nome, inicio, fim, descricao, status, ponto, id_usuario FROM Rotina WHERE status=:status";
            Query query = em.createNativeQuery(sql, Rotina.class);
            query.setParameter("status", status);
            List<Rotina> list = query.getResultList();
            Logger.info("RotinaDaoNativeImpl.findByStatus - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Rotina> findByPonto(Integer ponto) {
        Logger.info("RotinaDaoNativeImpl.findByPonto - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_rotina, nome, inicio, fim, descricao, status, ponto, id_usuario FROM Rotina WHERE ponto=:ponto";
            Query query = em.createNativeQuery(sql, Rotina.class);
            query.setParameter("ponto", ponto);
            List<Rotina> list = query.getResultList();
            Logger.info("RotinaDaoNativeImpl.findByPonto - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Rotina> findByIdUsuario(Integer idUsuario) {
        Logger.info("RotinaDaoNativeImpl.findByIdUsuario - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_rotina, nome, inicio, fim, descricao, status, ponto, id_usuario FROM Rotina WHERE id_usuario=:idUsuario";
            Query query = em.createNativeQuery(sql, Rotina.class);
            query.setParameter("idUsuario", idUsuario);
            List<Rotina> list = query.getResultList();
            Logger.info("RotinaDaoNativeImpl.findByIdUsuario - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Rotina> search(Rotina filtro) {
        return search(filtro, -1, -1);
    }

    @Override
    public List<Rotina> search(Rotina filtro, int page, int size) {
        Logger.info("RotinaDaoNativeImpl.search - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            StringBuilder sb = new StringBuilder("SELECT id_rotina, nome, inicio, fim, descricao, status, ponto, id_usuario FROM Rotina WHERE 1=1");
            Map<String, Object> params = new HashMap<>();
            if (filtro.getNome() != null && !filtro.getNome().isEmpty()) {
                sb.append(" AND nome=:nome");
                params.put("nome", filtro.getNome());
            }
            if (filtro.getInicio() != null) {
                sb.append(" AND inicio=:inicio");
                params.put("inicio", filtro.getInicio());
            }
            if (filtro.getFim() != null) {
                sb.append(" AND fim=:fim");
                params.put("fim", filtro.getFim());
            }
            if (filtro.getDescricao() != null && !filtro.getDescricao().isEmpty()) {
                sb.append(" AND descricao=:descricao");
                params.put("descricao", filtro.getDescricao());
            }
            if (filtro.getStatus() != null) {
                sb.append(" AND status=:status");
                params.put("status", filtro.getStatus());
            }
            if (filtro.getPonto() != null) {
                sb.append(" AND ponto=:ponto");
                params.put("ponto", filtro.getPonto());
            }
            if (filtro.getIdUsuario() != null) {
                sb.append(" AND id_usuario=:idUsuario");
                params.put("idUsuario", filtro.getIdUsuario());
            }
            if (page >= 0 && size > 0) {
                sb.append(" LIMIT :limit OFFSET :offset");
            }
            Query query = em.createNativeQuery(sb.toString(), Rotina.class);
            for (Map.Entry<String, Object> e : params.entrySet()) {
                query.setParameter(e.getKey(), e.getValue());
            }
            if (page >= 0 && size > 0) {
                query.setParameter("limit", size);
                query.setParameter("offset", page * size);
            }
            List<Rotina> list = query.getResultList();
            Logger.info("RotinaDaoNativeImpl.search - sucesso");
            return list;
        } finally {
            em.close();
        }
    }
}
