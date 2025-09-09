// path: src/main/java/dao/impl/ObjetoDaoNativeImpl.java
package dao.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dao.api.ObjetoDao;
import exception.ObjetoException;
import infra.EntityManagerUtil;
import infra.Logger;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import model.Objeto;

public class ObjetoDaoNativeImpl implements ObjetoDao {

    @Override
    public void create(Objeto objeto) throws ObjetoException {
        Logger.info("ObjetoDaoNativeImpl.create - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            String sql = "INSERT INTO Objeto (id_objeto, nome, tipo, valor, descricao, foto) VALUES (:id, :nome, :tipo, :valor, :descricao, :foto)";
            Query query = em.createNativeQuery(sql);
            query.setParameter("id", objeto.getIdObjeto());
            query.setParameter("nome", objeto.getNome());
            query.setParameter("tipo", objeto.getTipo());
            query.setParameter("valor", objeto.getValor());
            query.setParameter("descricao", objeto.getDescricao());
            query.setParameter("foto", objeto.getFoto());
            query.executeUpdate();
            em.getTransaction().commit();
            Logger.info("ObjetoDaoNativeImpl.create - sucesso");
        } catch (Exception e) {
            em.getTransaction().rollback();
            Logger.error("ObjetoDaoNativeImpl.create - erro", e);
            throw new ObjetoException("Erro ao criar Objeto", e);
        } finally {
            em.close();
        }
    }

    @Override
    public Objeto update(Objeto objeto) throws ObjetoException {
        Logger.info("ObjetoDaoNativeImpl.update - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            String sql = "UPDATE Objeto SET nome=:nome, tipo=:tipo, valor=:valor, descricao=:descricao, foto=:foto WHERE id_objeto=:id";
            Query query = em.createNativeQuery(sql);
            query.setParameter("nome", objeto.getNome());
            query.setParameter("tipo", objeto.getTipo());
            query.setParameter("valor", objeto.getValor());
            query.setParameter("descricao", objeto.getDescricao());
            query.setParameter("foto", objeto.getFoto());
            query.setParameter("id", objeto.getIdObjeto());
            int updated = query.executeUpdate();
            if (updated == 0) {
                throw new ObjetoException("Objeto não encontrado: id=" + objeto.getIdObjeto());
            }
            em.getTransaction().commit();
            Logger.info("ObjetoDaoNativeImpl.update - sucesso");
            return findById(objeto.getIdObjeto());
        } catch (ObjetoException e) {
            em.getTransaction().rollback();
            Logger.error("ObjetoDaoNativeImpl.update - erro", e);
            throw e;
        } catch (Exception e) {
            em.getTransaction().rollback();
            Logger.error("ObjetoDaoNativeImpl.update - erro", e);
            throw new ObjetoException("Erro ao atualizar Objeto", e);
        } finally {
            em.close();
        }
    }

    @Override
    public void deleteById(Integer id) throws ObjetoException {
        Logger.info("ObjetoDaoNativeImpl.deleteById - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            String sql = "DELETE FROM Objeto WHERE id_objeto=:id";
            Query query = em.createNativeQuery(sql);
            query.setParameter("id", id);
            int deleted = query.executeUpdate();
            if (deleted == 0) {
                throw new ObjetoException("Objeto não encontrado: id=" + id);
            }
            em.getTransaction().commit();
            Logger.info("ObjetoDaoNativeImpl.deleteById - sucesso");
        } catch (ObjetoException e) {
            em.getTransaction().rollback();
            Logger.error("ObjetoDaoNativeImpl.deleteById - erro", e);
            throw e;
        } catch (Exception e) {
            em.getTransaction().rollback();
            Logger.error("ObjetoDaoNativeImpl.deleteById - erro", e);
            throw new ObjetoException("Erro ao deletar Objeto", e);
        } finally {
            em.close();
        }
    }

    @Override
    public Objeto findById(Integer id) throws ObjetoException {
        Logger.info("ObjetoDaoNativeImpl.findById - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_objeto, nome, tipo, valor, descricao FROM Objeto WHERE id_objeto=:id";
            Query query = em.createNativeQuery(sql, Objeto.class);
            query.setParameter("id", id);
            Objeto o = (Objeto) query.getSingleResult();
            Logger.info("ObjetoDaoNativeImpl.findById - sucesso");
            return o;
        } catch (Exception e) {
            Logger.error("ObjetoDaoNativeImpl.findById - erro", e);
            throw new ObjetoException("Objeto não encontrado: id=" + id, e);
        } finally {
            em.close();
        }
    }

    @Override
    public Objeto findWithFotoById(Integer id) throws ObjetoException {
        Logger.info("ObjetoDaoNativeImpl.findWithFotoById - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_objeto, nome, tipo, valor, descricao, foto FROM Objeto WHERE id_objeto=:id";
            Query query = em.createNativeQuery(sql, Objeto.class);
            query.setParameter("id", id);
            Objeto o = (Objeto) query.getSingleResult();
            Logger.info("ObjetoDaoNativeImpl.findWithFotoById - sucesso");
            return o;
        } catch (Exception e) {
            Logger.error("ObjetoDaoNativeImpl.findWithFotoById - erro", e);
            throw new ObjetoException("Objeto não encontrado: id=" + id, e);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Objeto> findAll() {
        Logger.info("ObjetoDaoNativeImpl.findAll - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_objeto, nome, tipo, valor, descricao FROM Objeto";
            Query query = em.createNativeQuery(sql, Objeto.class);
            List<Objeto> list = query.getResultList();
            Logger.info("ObjetoDaoNativeImpl.findAll - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Objeto> findAll(int page, int size) {
        Logger.info("ObjetoDaoNativeImpl.findAll(page) - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_objeto, nome, tipo, valor, descricao FROM Objeto LIMIT :limit OFFSET :offset";
            Query query = em.createNativeQuery(sql, Objeto.class);
            query.setParameter("limit", size);
            query.setParameter("offset", page * size);
            List<Objeto> list = query.getResultList();
            Logger.info("ObjetoDaoNativeImpl.findAll(page) - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Objeto> findByNome(String nome) {
        Logger.info("ObjetoDaoNativeImpl.findByNome - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_objeto, nome, tipo, valor, descricao FROM Objeto WHERE nome=:nome";
            Query query = em.createNativeQuery(sql, Objeto.class);
            query.setParameter("nome", nome);
            List<Objeto> list = query.getResultList();
            Logger.info("ObjetoDaoNativeImpl.findByNome - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Objeto> findByTipo(Integer tipo) {
        Logger.info("ObjetoDaoNativeImpl.findByTipo - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_objeto, nome, tipo, valor, descricao FROM Objeto WHERE tipo=:tipo";
            Query query = em.createNativeQuery(sql, Objeto.class);
            query.setParameter("tipo", tipo);
            List<Objeto> list = query.getResultList();
            Logger.info("ObjetoDaoNativeImpl.findByTipo - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Objeto> findByValor(BigDecimal valor) {
        Logger.info("ObjetoDaoNativeImpl.findByValor - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_objeto, nome, tipo, valor, descricao FROM Objeto WHERE valor=:valor";
            Query query = em.createNativeQuery(sql, Objeto.class);
            query.setParameter("valor", valor);
            List<Objeto> list = query.getResultList();
            Logger.info("ObjetoDaoNativeImpl.findByValor - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Objeto> findByDescricao(String descricao) {
        Logger.info("ObjetoDaoNativeImpl.findByDescricao - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_objeto, nome, tipo, valor, descricao FROM Objeto WHERE descricao=:descricao";
            Query query = em.createNativeQuery(sql, Objeto.class);
            query.setParameter("descricao", descricao);
            List<Objeto> list = query.getResultList();
            Logger.info("ObjetoDaoNativeImpl.findByDescricao - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Objeto> findByFoto(byte[] foto) {
        Logger.info("ObjetoDaoNativeImpl.findByFoto - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_objeto, nome, tipo, valor, descricao, foto FROM Objeto WHERE foto=:foto";
            Query query = em.createNativeQuery(sql, Objeto.class);
            query.setParameter("foto", foto);
            List<Objeto> list = query.getResultList();
            Logger.info("ObjetoDaoNativeImpl.findByFoto - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Objeto> search(Objeto filtro) {
        return search(filtro, -1, -1);
    }

    @Override
    public List<Objeto> search(Objeto filtro, int page, int size) {
        Logger.info("ObjetoDaoNativeImpl.search - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            StringBuilder sb = new StringBuilder("SELECT id_objeto, nome, tipo, valor, descricao FROM Objeto WHERE 1=1");
            Map<String, Object> params = new HashMap<>();
            if (filtro.getNome() != null && !filtro.getNome().isEmpty()) {
                sb.append(" AND nome=:nome");
                params.put("nome", filtro.getNome());
            }
            if (filtro.getTipo() != null) {
                sb.append(" AND tipo=:tipo");
                params.put("tipo", filtro.getTipo());
            }
            if (filtro.getValor() != null) {
                sb.append(" AND valor=:valor");
                params.put("valor", filtro.getValor());
            }
            if (filtro.getDescricao() != null && !filtro.getDescricao().isEmpty()) {
                sb.append(" AND descricao=:descricao");
                params.put("descricao", filtro.getDescricao());
            }
            if (filtro.getFoto() != null) {
                sb.append(" AND foto=:foto");
                params.put("foto", filtro.getFoto());
            }
            if (page >= 0 && size > 0) {
                sb.append(" LIMIT :limit OFFSET :offset");
            }
            Query query = em.createNativeQuery(sb.toString(), Objeto.class);
            for (Map.Entry<String, Object> e : params.entrySet()) {
                query.setParameter(e.getKey(), e.getValue());
            }
            if (page >= 0 && size > 0) {
                query.setParameter("limit", size);
                query.setParameter("offset", page * size);
            }
            List<Objeto> list = query.getResultList();
            Logger.info("ObjetoDaoNativeImpl.search - sucesso");
            return list;
        } finally {
            em.close();
        }
    }
}
