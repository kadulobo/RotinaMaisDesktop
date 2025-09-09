// path: src/main/java/dao/impl/IngredienteDaoNativeImpl.java
package dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dao.api.IngredienteDao;
import exception.IngredienteException;
import infra.EntityManagerUtil;
import infra.Logger;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import model.Ingrediente;

public class IngredienteDaoNativeImpl implements IngredienteDao {

    @Override
    public void create(Ingrediente ingrediente) throws IngredienteException {
        Logger.info("IngredienteDaoNativeImpl.create - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            String sql = "INSERT INTO Ingrediente (id_ingrediente, nome, descricao, foto) " +
                    "VALUES (:id, :nome, :descricao, :foto)";
            Query query = em.createNativeQuery(sql);
            query.setParameter("id", ingrediente.getIdIngrediente());
            query.setParameter("nome", ingrediente.getNome());
            query.setParameter("descricao", ingrediente.getDescricao());
            query.setParameter("foto", ingrediente.getFoto());
            query.executeUpdate();
            em.getTransaction().commit();
            Logger.info("IngredienteDaoNativeImpl.create - sucesso");
        } catch (Exception e) {
            em.getTransaction().rollback();
            Logger.error("IngredienteDaoNativeImpl.create - erro", e);
            throw new IngredienteException("Erro ao criar Ingrediente", e);
        } finally {
            em.close();
        }
    }

    @Override
    public Ingrediente update(Ingrediente ingrediente) throws IngredienteException {
        Logger.info("IngredienteDaoNativeImpl.update - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            String sql = "UPDATE Ingrediente SET nome=:nome, descricao=:descricao, foto=:foto WHERE id_ingrediente=:id";
            Query query = em.createNativeQuery(sql);
            query.setParameter("nome", ingrediente.getNome());
            query.setParameter("descricao", ingrediente.getDescricao());
            query.setParameter("foto", ingrediente.getFoto());
            query.setParameter("id", ingrediente.getIdIngrediente());
            int updated = query.executeUpdate();
            if (updated == 0) {
                throw new IngredienteException("Ingrediente não encontrado: id=" + ingrediente.getIdIngrediente());
            }
            em.getTransaction().commit();
            Logger.info("IngredienteDaoNativeImpl.update - sucesso");
            return findById(ingrediente.getIdIngrediente());
        } catch (IngredienteException e) {
            em.getTransaction().rollback();
            Logger.error("IngredienteDaoNativeImpl.update - erro", e);
            throw e;
        } catch (Exception e) {
            em.getTransaction().rollback();
            Logger.error("IngredienteDaoNativeImpl.update - erro", e);
            throw new IngredienteException("Erro ao atualizar Ingrediente", e);
        } finally {
            em.close();
        }
    }

    @Override
    public void deleteById(Integer id) throws IngredienteException {
        Logger.info("IngredienteDaoNativeImpl.deleteById - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            String sql = "DELETE FROM Ingrediente WHERE id_ingrediente=:id";
            Query query = em.createNativeQuery(sql);
            query.setParameter("id", id);
            int deleted = query.executeUpdate();
            if (deleted == 0) {
                throw new IngredienteException("Ingrediente não encontrado: id=" + id);
            }
            em.getTransaction().commit();
            Logger.info("IngredienteDaoNativeImpl.deleteById - sucesso");
        } catch (IngredienteException e) {
            em.getTransaction().rollback();
            Logger.error("IngredienteDaoNativeImpl.deleteById - erro", e);
            throw e;
        } catch (Exception e) {
            em.getTransaction().rollback();
            Logger.error("IngredienteDaoNativeImpl.deleteById - erro", e);
            throw new IngredienteException("Erro ao deletar Ingrediente", e);
        } finally {
            em.close();
        }
    }

    @Override
    public Ingrediente findById(Integer id) throws IngredienteException {
        Logger.info("IngredienteDaoNativeImpl.findById - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_ingrediente, nome, descricao FROM Ingrediente WHERE id_ingrediente=:id";
            Query query = em.createNativeQuery(sql, Ingrediente.class);
            query.setParameter("id", id);
            Ingrediente i = (Ingrediente) query.getSingleResult();
            Logger.info("IngredienteDaoNativeImpl.findById - sucesso");
            return i;
        } catch (Exception e) {
            Logger.error("IngredienteDaoNativeImpl.findById - erro", e);
            throw new IngredienteException("Ingrediente não encontrado: id=" + id, e);
        } finally {
            em.close();
        }
    }

    @Override
    public Ingrediente findWithFotoById(Integer id) throws IngredienteException {
        Logger.info("IngredienteDaoNativeImpl.findWithFotoById - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_ingrediente, nome, descricao, foto FROM Ingrediente WHERE id_ingrediente=:id";
            Query query = em.createNativeQuery(sql, Ingrediente.class);
            query.setParameter("id", id);
            Ingrediente i = (Ingrediente) query.getSingleResult();
            Logger.info("IngredienteDaoNativeImpl.findWithFotoById - sucesso");
            return i;
        } catch (Exception e) {
            Logger.error("IngredienteDaoNativeImpl.findWithFotoById - erro", e);
            throw new IngredienteException("Ingrediente não encontrado: id=" + id, e);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Ingrediente> findAll() {
        Logger.info("IngredienteDaoNativeImpl.findAll - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_ingrediente, nome, descricao FROM Ingrediente";
            Query query = em.createNativeQuery(sql, Ingrediente.class);
            List<Ingrediente> list = query.getResultList();
            Logger.info("IngredienteDaoNativeImpl.findAll - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Ingrediente> findAll(int page, int size) {
        Logger.info("IngredienteDaoNativeImpl.findAll(page) - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_ingrediente, nome, descricao FROM Ingrediente LIMIT :limit OFFSET :offset";
            Query query = em.createNativeQuery(sql, Ingrediente.class);
            query.setParameter("limit", size);
            query.setParameter("offset", page * size);
            List<Ingrediente> list = query.getResultList();
            Logger.info("IngredienteDaoNativeImpl.findAll(page) - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Ingrediente> findByFoto(byte[] foto) {
        Logger.info("IngredienteDaoNativeImpl.findByFoto - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_ingrediente, nome, descricao, foto FROM Ingrediente WHERE foto=:foto";
            Query query = em.createNativeQuery(sql, Ingrediente.class);
            query.setParameter("foto", foto);
            List<Ingrediente> list = query.getResultList();
            Logger.info("IngredienteDaoNativeImpl.findByFoto - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Ingrediente> findByNome(String nome) {
        Logger.info("IngredienteDaoNativeImpl.findByNome - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_ingrediente, nome, descricao FROM Ingrediente WHERE nome=:nome";
            Query query = em.createNativeQuery(sql, Ingrediente.class);
            query.setParameter("nome", nome);
            List<Ingrediente> list = query.getResultList();
            Logger.info("IngredienteDaoNativeImpl.findByNome - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Ingrediente> findByDescricao(String descricao) {
        Logger.info("IngredienteDaoNativeImpl.findByDescricao - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_ingrediente, nome, descricao FROM Ingrediente WHERE descricao=:descricao";
            Query query = em.createNativeQuery(sql, Ingrediente.class);
            query.setParameter("descricao", descricao);
            List<Ingrediente> list = query.getResultList();
            Logger.info("IngredienteDaoNativeImpl.findByDescricao - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Ingrediente> search(Ingrediente filtro) {
        return search(filtro, -1, -1);
    }

    @Override
    public List<Ingrediente> search(Ingrediente filtro, int page, int size) {
        Logger.info("IngredienteDaoNativeImpl.search - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            StringBuilder sb = new StringBuilder("SELECT id_ingrediente, nome, descricao FROM Ingrediente WHERE 1=1");
            Map<String, Object> params = new HashMap<>();
            if (filtro.getFoto() != null) {
                sb.append(" AND foto=:foto");
                params.put("foto", filtro.getFoto());
            }
            if (filtro.getNome() != null && !filtro.getNome().isEmpty()) {
                sb.append(" AND nome=:nome");
                params.put("nome", filtro.getNome());
            }
            if (filtro.getDescricao() != null && !filtro.getDescricao().isEmpty()) {
                sb.append(" AND descricao=:descricao");
                params.put("descricao", filtro.getDescricao());
            }
            if (page >= 0 && size > 0) {
                sb.append(" LIMIT :limit OFFSET :offset");
            }
            Query query = em.createNativeQuery(sb.toString(), Ingrediente.class);
            for (Map.Entry<String, Object> e : params.entrySet()) {
                query.setParameter(e.getKey(), e.getValue());
            }
            if (page >= 0 && size > 0) {
                query.setParameter("limit", size);
                query.setParameter("offset", page * size);
            }
            List<Ingrediente> list = query.getResultList();
            Logger.info("IngredienteDaoNativeImpl.search - sucesso");
            return list;
        } finally {
            em.close();
        }
    }
}
