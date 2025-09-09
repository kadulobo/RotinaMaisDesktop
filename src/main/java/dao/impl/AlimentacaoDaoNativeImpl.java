// path: src/main/java/dao/impl/AlimentacaoDaoNativeImpl.java
package dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dao.api.AlimentacaoDao;
import exception.AlimentacaoException;
import infra.EntityManagerUtil;
import infra.Logger;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import model.Alimentacao;
import org.hibernate.query.NativeQuery;
import org.hibernate.type.StandardBasicTypes;

public class AlimentacaoDaoNativeImpl implements AlimentacaoDao {

    @Override
    public void create(Alimentacao alimentacao) throws AlimentacaoException {
        Logger.info("AlimentacaoDaoNativeImpl.create - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            String sql = "INSERT INTO Alimentacao (status, nome, link, video, preparo, id_rotina) " +
                    "VALUES (:status, :nome, :link, :video, :preparo, CAST(:idRotina AS INTEGER))";
            NativeQuery<?> query = (NativeQuery<?>) em.createNativeQuery(sql);
            query.setParameter("status", alimentacao.getStatus());
            query.setParameter("nome", alimentacao.getNome());
            query.setParameter("link", alimentacao.getLink());
            query.setParameter("video", alimentacao.getVideo());
            query.setParameter("preparo", alimentacao.getPreparo());
            Integer idRotina = alimentacao.getIdRotina();
            query.setParameter("idRotina", idRotina, StandardBasicTypes.INTEGER);
            query.executeUpdate();
            em.getTransaction().commit();
            Logger.info("AlimentacaoDaoNativeImpl.create - sucesso");
        } catch (Exception e) {
            em.getTransaction().rollback();
            Logger.error("AlimentacaoDaoNativeImpl.create - erro", e);
            throw new AlimentacaoException("Erro ao criar Alimentacao", e);
        } finally {
            em.close();
        }
    }

    @Override
    public Alimentacao update(Alimentacao alimentacao) throws AlimentacaoException {
        Logger.info("AlimentacaoDaoNativeImpl.update - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            String sql = "UPDATE Alimentacao SET status=:status, nome=:nome, link=:link, video=:video, preparo=:preparo, id_rotina=CAST(:idRotina AS INTEGER) WHERE id_alimentacao=:id";
            NativeQuery<?> query = (NativeQuery<?>) em.createNativeQuery(sql);
            query.setParameter("status", alimentacao.getStatus());
            query.setParameter("nome", alimentacao.getNome());
            query.setParameter("link", alimentacao.getLink());
            query.setParameter("video", alimentacao.getVideo());
            query.setParameter("preparo", alimentacao.getPreparo());
            Integer idRotina = alimentacao.getIdRotina();
            query.setParameter("idRotina", idRotina, StandardBasicTypes.INTEGER);
            query.setParameter("id", alimentacao.getIdAlimentacao());
            int updated = query.executeUpdate();
            if (updated == 0) {
                throw new AlimentacaoException("Alimentacao não encontrada: id=" + alimentacao.getIdAlimentacao());
            }
            em.getTransaction().commit();
            Logger.info("AlimentacaoDaoNativeImpl.update - sucesso");
            return findById(alimentacao.getIdAlimentacao());
        } catch (AlimentacaoException e) {
            em.getTransaction().rollback();
            Logger.error("AlimentacaoDaoNativeImpl.update - erro", e);
            throw e;
        } catch (Exception e) {
            em.getTransaction().rollback();
            Logger.error("AlimentacaoDaoNativeImpl.update - erro", e);
            throw new AlimentacaoException("Erro ao atualizar Alimentacao", e);
        } finally {
            em.close();
        }
    }

    @Override
    public void deleteById(Integer id) throws AlimentacaoException {
        Logger.info("AlimentacaoDaoNativeImpl.deleteById - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            String sql = "DELETE FROM Alimentacao WHERE id_alimentacao=:id";
            Query query = em.createNativeQuery(sql);
            query.setParameter("id", id);
            int deleted = query.executeUpdate();
            if (deleted == 0) {
                throw new AlimentacaoException("Alimentacao não encontrada: id=" + id);
            }
            em.getTransaction().commit();
            Logger.info("AlimentacaoDaoNativeImpl.deleteById - sucesso");
        } catch (AlimentacaoException e) {
            em.getTransaction().rollback();
            Logger.error("AlimentacaoDaoNativeImpl.deleteById - erro", e);
            throw e;
        } catch (Exception e) {
            em.getTransaction().rollback();
            Logger.error("AlimentacaoDaoNativeImpl.deleteById - erro", e);
            throw new AlimentacaoException("Erro ao deletar Alimentacao", e);
        } finally {
            em.close();
        }
    }

    @Override
    public Alimentacao findById(Integer id) throws AlimentacaoException {
        Logger.info("AlimentacaoDaoNativeImpl.findById - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_alimentacao, status, nome, link, preparo, id_rotina FROM Alimentacao WHERE id_alimentacao=:id";
            Query query = em.createNativeQuery(sql, Alimentacao.class);
            query.setParameter("id", id);
            Alimentacao a = (Alimentacao) query.getSingleResult();
            Logger.info("AlimentacaoDaoNativeImpl.findById - sucesso");
            return a;
        } catch (Exception e) {
            Logger.error("AlimentacaoDaoNativeImpl.findById - erro", e);
            throw new AlimentacaoException("Alimentacao não encontrada: id=" + id, e);
        } finally {
            em.close();
        }
    }

    @Override
    public Alimentacao findWithVideoById(Integer id) throws AlimentacaoException {
        Logger.info("AlimentacaoDaoNativeImpl.findWithVideoById - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_alimentacao, status, nome, link, video, preparo, id_rotina FROM Alimentacao WHERE id_alimentacao=:id";
            Query query = em.createNativeQuery(sql, Alimentacao.class);
            query.setParameter("id", id);
            Alimentacao a = (Alimentacao) query.getSingleResult();
            Logger.info("AlimentacaoDaoNativeImpl.findWithVideoById - sucesso");
            return a;
        } catch (Exception e) {
            Logger.error("AlimentacaoDaoNativeImpl.findWithVideoById - erro", e);
            throw new AlimentacaoException("Alimentacao não encontrada: id=" + id, e);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Alimentacao> findAll() {
        Logger.info("AlimentacaoDaoNativeImpl.findAll - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_alimentacao, status, nome, link, preparo, id_rotina FROM Alimentacao";
            Query query = em.createNativeQuery(sql, Alimentacao.class);
            List<Alimentacao> list = query.getResultList();
            Logger.info("AlimentacaoDaoNativeImpl.findAll - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Alimentacao> findAll(int page, int size) {
        Logger.info("AlimentacaoDaoNativeImpl.findAll(page) - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_alimentacao, status, nome, link, preparo, id_rotina FROM Alimentacao LIMIT :limit OFFSET :offset";
            Query query = em.createNativeQuery(sql, Alimentacao.class);
            query.setParameter("limit", size);
            query.setParameter("offset", page * size);
            List<Alimentacao> list = query.getResultList();
            Logger.info("AlimentacaoDaoNativeImpl.findAll(page) - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Alimentacao> findByStatus(Integer status) {
        Logger.info("AlimentacaoDaoNativeImpl.findByStatus - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_alimentacao, status, nome, link, preparo, id_rotina FROM Alimentacao WHERE status=:status";
            Query query = em.createNativeQuery(sql, Alimentacao.class);
            query.setParameter("status", status);
            List<Alimentacao> list = query.getResultList();
            Logger.info("AlimentacaoDaoNativeImpl.findByStatus - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Alimentacao> findByNome(String nome) {
        Logger.info("AlimentacaoDaoNativeImpl.findByNome - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_alimentacao, status, nome, link, preparo, id_rotina FROM Alimentacao WHERE nome=:nome";
            Query query = em.createNativeQuery(sql, Alimentacao.class);
            query.setParameter("nome", nome);
            List<Alimentacao> list = query.getResultList();
            Logger.info("AlimentacaoDaoNativeImpl.findByNome - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Alimentacao> findByLink(String link) {
        Logger.info("AlimentacaoDaoNativeImpl.findByLink - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_alimentacao, status, nome, link, preparo, id_rotina FROM Alimentacao WHERE link=:link";
            Query query = em.createNativeQuery(sql, Alimentacao.class);
            query.setParameter("link", link);
            List<Alimentacao> list = query.getResultList();
            Logger.info("AlimentacaoDaoNativeImpl.findByLink - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Alimentacao> findByVideo(byte[] video) {
        Logger.info("AlimentacaoDaoNativeImpl.findByVideo - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_alimentacao, status, nome, link, video, preparo, id_rotina FROM Alimentacao WHERE video=:video";
            Query query = em.createNativeQuery(sql, Alimentacao.class);
            query.setParameter("video", video);
            List<Alimentacao> list = query.getResultList();
            Logger.info("AlimentacaoDaoNativeImpl.findByVideo - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Alimentacao> findByPreparo(String preparo) {
        Logger.info("AlimentacaoDaoNativeImpl.findByPreparo - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_alimentacao, status, nome, link, preparo, id_rotina FROM Alimentacao WHERE preparo=:preparo";
            Query query = em.createNativeQuery(sql, Alimentacao.class);
            query.setParameter("preparo", preparo);
            List<Alimentacao> list = query.getResultList();
            Logger.info("AlimentacaoDaoNativeImpl.findByPreparo - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Alimentacao> findByIdRotina(Integer idRotina) {
        Logger.info("AlimentacaoDaoNativeImpl.findByIdRotina - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_alimentacao, status, nome, link, preparo, id_rotina FROM Alimentacao WHERE id_rotina=:idRotina";
            Query query = em.createNativeQuery(sql, Alimentacao.class);
            query.setParameter("idRotina", idRotina);
            List<Alimentacao> list = query.getResultList();
            Logger.info("AlimentacaoDaoNativeImpl.findByIdRotina - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Alimentacao> search(Alimentacao filtro) {
        return search(filtro, -1, -1);
    }

    @Override
    public List<Alimentacao> search(Alimentacao filtro, int page, int size) {
        Logger.info("AlimentacaoDaoNativeImpl.search - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            StringBuilder sb = new StringBuilder("SELECT id_alimentacao, status, nome, link, preparo, id_rotina FROM Alimentacao WHERE 1=1");
            Map<String, Object> params = new HashMap<>();
            if (filtro.getStatus() != null) {
                sb.append(" AND status=:status");
                params.put("status", filtro.getStatus());
            }
            if (filtro.getNome() != null && !filtro.getNome().isEmpty()) {
                sb.append(" AND nome=:nome");
                params.put("nome", filtro.getNome());
            }
            if (filtro.getLink() != null && !filtro.getLink().isEmpty()) {
                sb.append(" AND link=:link");
                params.put("link", filtro.getLink());
            }
            if (filtro.getVideo() != null) {
                sb.append(" AND video=:video");
                params.put("video", filtro.getVideo());
            }
            if (filtro.getPreparo() != null && !filtro.getPreparo().isEmpty()) {
                sb.append(" AND preparo=:preparo");
                params.put("preparo", filtro.getPreparo());
            }
            if (filtro.getIdRotina() != null) {
                sb.append(" AND id_rotina=:idRotina");
                params.put("idRotina", filtro.getIdRotina());
            }
            if (page >= 0 && size > 0) {
                sb.append(" LIMIT :limit OFFSET :offset");
            }
            Query query = em.createNativeQuery(sb.toString(), Alimentacao.class);
            for (Map.Entry<String, Object> e : params.entrySet()) {
                query.setParameter(e.getKey(), e.getValue());
            }
            if (page >= 0 && size > 0) {
                query.setParameter("limit", size);
                query.setParameter("offset", page * size);
            }
            List<Alimentacao> list = query.getResultList();
            Logger.info("AlimentacaoDaoNativeImpl.search - sucesso");
            return list;
        } finally {
            em.close();
        }
    }
}
