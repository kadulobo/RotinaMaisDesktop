// path: src/main/java/dao/impl/EventoDaoNativeImpl.java
package dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dao.api.EventoDao;
import exception.EventoException;
import infra.EntityManagerUtil;
import infra.Logger;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import model.Evento;

public class EventoDaoNativeImpl implements EventoDao {

    @Override
    public void create(Evento evento) throws EventoException {
        Logger.info("EventoDaoNativeImpl.create - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            String sql = "INSERT INTO Evento (id_evento, vantagem, foto, nome, descricao, data_criacao, id_categoria) " +
                    "VALUES (:id, :vantagem, :foto, :nome, :descricao, :dataCriacao, :idCategoria)";
            Query query = em.createNativeQuery(sql);
            query.setParameter("id", evento.getIdEvento());
            query.setParameter("vantagem", evento.getVantagem());
            query.setParameter("foto", evento.getFoto());
            query.setParameter("nome", evento.getNome());
            query.setParameter("descricao", evento.getDescricao());
            query.setParameter("dataCriacao", evento.getDataCriacao());
            query.setParameter("idCategoria", evento.getCategoria() != null ? evento.getCategoria().getIdCategoria() : null);
            query.executeUpdate();
            em.getTransaction().commit();
            Logger.info("EventoDaoNativeImpl.create - sucesso");
        } catch (Exception e) {
            em.getTransaction().rollback();
            Logger.error("EventoDaoNativeImpl.create - erro", e);
            throw new EventoException("Erro ao criar Evento", e);
        } finally {
            em.close();
        }
    }

    @Override
    public Evento update(Evento evento) throws EventoException {
        Logger.info("EventoDaoNativeImpl.update - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            String sql = "UPDATE Evento SET vantagem=:vantagem, foto=:foto, nome=:nome, descricao=:descricao, " +
                    "data_criacao=:dataCriacao, id_categoria=:idCategoria WHERE id_evento=:id";
            Query query = em.createNativeQuery(sql);
            query.setParameter("vantagem", evento.getVantagem());
            query.setParameter("foto", evento.getFoto());
            query.setParameter("nome", evento.getNome());
            query.setParameter("descricao", evento.getDescricao());
            query.setParameter("dataCriacao", evento.getDataCriacao());
            query.setParameter("idCategoria", evento.getCategoria() != null ? evento.getCategoria().getIdCategoria() : null);
            query.setParameter("id", evento.getIdEvento());
            int updated = query.executeUpdate();
            if (updated == 0) {
                throw new EventoException("Evento não encontrado: id=" + evento.getIdEvento());
            }
            em.getTransaction().commit();
            Logger.info("EventoDaoNativeImpl.update - sucesso");
            return findById(evento.getIdEvento());
        } catch (EventoException e) {
            em.getTransaction().rollback();
            Logger.error("EventoDaoNativeImpl.update - erro", e);
            throw e;
        } catch (Exception e) {
            em.getTransaction().rollback();
            Logger.error("EventoDaoNativeImpl.update - erro", e);
            throw new EventoException("Erro ao atualizar Evento", e);
        } finally {
            em.close();
        }
    }

    @Override
    public void deleteById(Integer id) throws EventoException {
        Logger.info("EventoDaoNativeImpl.deleteById - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            String sql = "DELETE FROM Evento WHERE id_evento=:id";
            Query query = em.createNativeQuery(sql);
            query.setParameter("id", id);
            int deleted = query.executeUpdate();
            if (deleted == 0) {
                throw new EventoException("Evento não encontrado: id=" + id);
            }
            em.getTransaction().commit();
            Logger.info("EventoDaoNativeImpl.deleteById - sucesso");
        } catch (EventoException e) {
            em.getTransaction().rollback();
            Logger.error("EventoDaoNativeImpl.deleteById - erro", e);
            throw e;
        } catch (Exception e) {
            em.getTransaction().rollback();
            Logger.error("EventoDaoNativeImpl.deleteById - erro", e);
            throw new EventoException("Erro ao deletar Evento", e);
        } finally {
            em.close();
        }
    }

    @Override
    public Evento findById(Integer id) throws EventoException {
        Logger.info("EventoDaoNativeImpl.findById - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_evento, vantagem, nome, descricao, data_criacao, id_categoria FROM Evento WHERE id_evento=:id";
            Query query = em.createNativeQuery(sql, Evento.class);
            query.setParameter("id", id);
            Evento e = (Evento) query.getSingleResult();
            Logger.info("EventoDaoNativeImpl.findById - sucesso");
            return e;
        } catch (Exception e) {
            Logger.error("EventoDaoNativeImpl.findById - erro", e);
            throw new EventoException("Evento não encontrado: id=" + id, e);
        } finally {
            em.close();
        }
    }

    @Override
    public Evento findWithBlobsById(Integer id) throws EventoException {
        Logger.info("EventoDaoNativeImpl.findWithBlobsById - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_evento, vantagem, foto, nome, descricao, data_criacao, id_categoria FROM Evento WHERE id_evento=:id";
            Query query = em.createNativeQuery(sql, Evento.class);
            query.setParameter("id", id);
            Evento e = (Evento) query.getSingleResult();
            Logger.info("EventoDaoNativeImpl.findWithBlobsById - sucesso");
            return e;
        } catch (Exception e) {
            Logger.error("EventoDaoNativeImpl.findWithBlobsById - erro", e);
            throw new EventoException("Evento não encontrado: id=" + id, e);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Evento> findAll() {
        Logger.info("EventoDaoNativeImpl.findAll - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_evento, vantagem, nome, descricao, data_criacao, id_categoria FROM Evento";
            Query query = em.createNativeQuery(sql, Evento.class);
            List<Evento> list = query.getResultList();
            Logger.info("EventoDaoNativeImpl.findAll - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Evento> findAll(int page, int size) {
        Logger.info("EventoDaoNativeImpl.findAll(page) - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_evento, vantagem, nome, descricao, data_criacao, id_categoria FROM Evento LIMIT :limit OFFSET :offset";
            Query query = em.createNativeQuery(sql, Evento.class);
            query.setParameter("limit", size);
            query.setParameter("offset", page * size);
            List<Evento> list = query.getResultList();
            Logger.info("EventoDaoNativeImpl.findAll(page) - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Evento> findByVantagem(Boolean vantagem) {
        Logger.info("EventoDaoNativeImpl.findByVantagem - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_evento, vantagem, nome, descricao, data_criacao, id_categoria FROM Evento WHERE vantagem=:vantagem";
            Query query = em.createNativeQuery(sql, Evento.class);
            query.setParameter("vantagem", vantagem);
            List<Evento> list = query.getResultList();
            Logger.info("EventoDaoNativeImpl.findByVantagem - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Evento> findByFoto(byte[] foto) {
        Logger.info("EventoDaoNativeImpl.findByFoto - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_evento, vantagem, foto, nome, descricao, data_criacao, id_categoria FROM Evento WHERE foto=:foto";
            Query query = em.createNativeQuery(sql, Evento.class);
            query.setParameter("foto", foto);
            List<Evento> list = query.getResultList();
            Logger.info("EventoDaoNativeImpl.findByFoto - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Evento> findByNome(String nome) {
        Logger.info("EventoDaoNativeImpl.findByNome - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_evento, vantagem, nome, descricao, data_criacao, id_categoria FROM Evento WHERE nome=:nome";
            Query query = em.createNativeQuery(sql, Evento.class);
            query.setParameter("nome", nome);
            List<Evento> list = query.getResultList();
            Logger.info("EventoDaoNativeImpl.findByNome - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Evento> findByDescricao(String descricao) {
        Logger.info("EventoDaoNativeImpl.findByDescricao - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_evento, vantagem, nome, descricao, data_criacao, id_categoria FROM Evento WHERE descricao=:descricao";
            Query query = em.createNativeQuery(sql, Evento.class);
            query.setParameter("descricao", descricao);
            List<Evento> list = query.getResultList();
            Logger.info("EventoDaoNativeImpl.findByDescricao - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Evento> findByDataCriacao(java.time.LocalDate dataCriacao) {
        Logger.info("EventoDaoNativeImpl.findByDataCriacao - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_evento, vantagem, nome, descricao, data_criacao, id_categoria FROM Evento WHERE data_criacao=:dataCriacao";
            Query query = em.createNativeQuery(sql, Evento.class);
            query.setParameter("dataCriacao", dataCriacao);
            List<Evento> list = query.getResultList();
            Logger.info("EventoDaoNativeImpl.findByDataCriacao - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Evento> findByIdCategoria(Integer idCategoria) {
        Logger.info("EventoDaoNativeImpl.findByIdCategoria - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_evento, vantagem, nome, descricao, data_criacao, id_categoria FROM Evento WHERE id_categoria=:idCategoria";
            Query query = em.createNativeQuery(sql, Evento.class);
            query.setParameter("idCategoria", idCategoria);
            List<Evento> list = query.getResultList();
            Logger.info("EventoDaoNativeImpl.findByIdCategoria - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Evento> search(Evento filtro) {
        return search(filtro, -1, -1);
    }

    @Override
    public List<Evento> search(Evento filtro, int page, int size) {
        Logger.info("EventoDaoNativeImpl.search - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            StringBuilder sb = new StringBuilder("SELECT id_evento, vantagem, nome, descricao, data_criacao, id_categoria FROM Evento WHERE 1=1");
            Map<String, Object> params = new HashMap<>();
            if (filtro.getVantagem() != null) {
                sb.append(" AND vantagem=:vantagem");
                params.put("vantagem", filtro.getVantagem());
            }
            if (filtro.getNome() != null && !filtro.getNome().isEmpty()) {
                sb.append(" AND nome=:nome");
                params.put("nome", filtro.getNome());
            }
            if (filtro.getDescricao() != null && !filtro.getDescricao().isEmpty()) {
                sb.append(" AND descricao=:descricao");
                params.put("descricao", filtro.getDescricao());
            }
            if (filtro.getDataCriacao() != null) {
                sb.append(" AND data_criacao=:dataCriacao");
                params.put("dataCriacao", filtro.getDataCriacao());
            }
            if (filtro.getCategoria() != null && filtro.getCategoria().getIdCategoria() != null) {
                sb.append(" AND id_categoria=:idCategoria");
                params.put("idCategoria", filtro.getCategoria().getIdCategoria());
            }
            if (page >= 0 && size > 0) {
                sb.append(" LIMIT :limit OFFSET :offset");
            }
            Query query = em.createNativeQuery(sb.toString(), Evento.class);
            for (Map.Entry<String, Object> e : params.entrySet()) {
                query.setParameter(e.getKey(), e.getValue());
            }
            if (page >= 0 && size > 0) {
                query.setParameter("limit", size);
                query.setParameter("offset", page * size);
            }
            List<Evento> list = query.getResultList();
            Logger.info("EventoDaoNativeImpl.search - sucesso");
            return list;
        } finally {
            em.close();
        }
    }
}
