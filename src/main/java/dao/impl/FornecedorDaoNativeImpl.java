// path: src/main/java/dao/impl/FornecedorDaoNativeImpl.java
package dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dao.api.FornecedorDao;
import exception.FornecedorException;
import infra.EntityManagerUtil;
import infra.Logger;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import model.Fornecedor;

public class FornecedorDaoNativeImpl implements FornecedorDao {

    @Override
    public void create(Fornecedor fornecedor) throws FornecedorException {
        Logger.info("FornecedorDaoNativeImpl.create - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            String sql = "INSERT INTO Fornecedor (id_fornecedor, nome, foto, endereco, online) " +
                    "VALUES (:id, :nome, :foto, :endereco, :online)";
            Query query = em.createNativeQuery(sql);
            query.setParameter("id", fornecedor.getIdFornecedor());
            query.setParameter("nome", fornecedor.getNome());
            query.setParameter("foto", fornecedor.getFoto());
            query.setParameter("endereco", fornecedor.getEndereco());
            query.setParameter("online", fornecedor.getOnline());
            query.executeUpdate();
            em.getTransaction().commit();
            Logger.info("FornecedorDaoNativeImpl.create - sucesso");
        } catch (Exception e) {
            em.getTransaction().rollback();
            Logger.error("FornecedorDaoNativeImpl.create - erro", e);
            throw new FornecedorException("Erro ao criar Fornecedor", e);
        } finally {
            em.close();
        }
    }

    @Override
    public Fornecedor update(Fornecedor fornecedor) throws FornecedorException {
        Logger.info("FornecedorDaoNativeImpl.update - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            String sql = "UPDATE Fornecedor SET nome=:nome, foto=:foto, endereco=:endereco, online=:online WHERE id_fornecedor=:id";
            Query query = em.createNativeQuery(sql);
            query.setParameter("nome", fornecedor.getNome());
            query.setParameter("foto", fornecedor.getFoto());
            query.setParameter("endereco", fornecedor.getEndereco());
            query.setParameter("online", fornecedor.getOnline());
            query.setParameter("id", fornecedor.getIdFornecedor());
            int updated = query.executeUpdate();
            if (updated == 0) {
                throw new FornecedorException("Fornecedor não encontrado: id=" + fornecedor.getIdFornecedor());
            }
            em.getTransaction().commit();
            Logger.info("FornecedorDaoNativeImpl.update - sucesso");
            return findById(fornecedor.getIdFornecedor());
        } catch (FornecedorException e) {
            em.getTransaction().rollback();
            Logger.error("FornecedorDaoNativeImpl.update - erro", e);
            throw e;
        } catch (Exception e) {
            em.getTransaction().rollback();
            Logger.error("FornecedorDaoNativeImpl.update - erro", e);
            throw new FornecedorException("Erro ao atualizar Fornecedor", e);
        } finally {
            em.close();
        }
    }

    @Override
    public void deleteById(Integer id) throws FornecedorException {
        Logger.info("FornecedorDaoNativeImpl.deleteById - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            String sql = "DELETE FROM Fornecedor WHERE id_fornecedor=:id";
            Query query = em.createNativeQuery(sql);
            query.setParameter("id", id);
            int deleted = query.executeUpdate();
            if (deleted == 0) {
                throw new FornecedorException("Fornecedor não encontrado: id=" + id);
            }
            em.getTransaction().commit();
            Logger.info("FornecedorDaoNativeImpl.deleteById - sucesso");
        } catch (FornecedorException e) {
            em.getTransaction().rollback();
            Logger.error("FornecedorDaoNativeImpl.deleteById - erro", e);
            throw e;
        } catch (Exception e) {
            em.getTransaction().rollback();
            Logger.error("FornecedorDaoNativeImpl.deleteById - erro", e);
            throw new FornecedorException("Erro ao deletar Fornecedor", e);
        } finally {
            em.close();
        }
    }

    @Override
    public Fornecedor findById(Integer id) throws FornecedorException {
        Logger.info("FornecedorDaoNativeImpl.findById - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_fornecedor, nome, endereco, online FROM Fornecedor WHERE id_fornecedor=:id";
            Query query = em.createNativeQuery(sql, Fornecedor.class);
            query.setParameter("id", id);
            Fornecedor f = (Fornecedor) query.getSingleResult();
            Logger.info("FornecedorDaoNativeImpl.findById - sucesso");
            return f;
        } catch (Exception e) {
            Logger.error("FornecedorDaoNativeImpl.findById - erro", e);
            throw new FornecedorException("Fornecedor não encontrado: id=" + id, e);
        } finally {
            em.close();
        }
    }

    @Override
    public Fornecedor findWithFotoById(Integer id) throws FornecedorException {
        Logger.info("FornecedorDaoNativeImpl.findWithFotoById - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_fornecedor, nome, foto, endereco, online FROM Fornecedor WHERE id_fornecedor=:id";
            Query query = em.createNativeQuery(sql, Fornecedor.class);
            query.setParameter("id", id);
            Fornecedor f = (Fornecedor) query.getSingleResult();
            Logger.info("FornecedorDaoNativeImpl.findWithFotoById - sucesso");
            return f;
        } catch (Exception e) {
            Logger.error("FornecedorDaoNativeImpl.findWithFotoById - erro", e);
            throw new FornecedorException("Fornecedor não encontrado: id=" + id, e);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Fornecedor> findAll() {
        Logger.info("FornecedorDaoNativeImpl.findAll - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_fornecedor, nome, endereco, online FROM Fornecedor";
            Query query = em.createNativeQuery(sql, Fornecedor.class);
            List<Fornecedor> list = query.getResultList();
            Logger.info("FornecedorDaoNativeImpl.findAll - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Fornecedor> findAll(int page, int size) {
        Logger.info("FornecedorDaoNativeImpl.findAll(page) - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_fornecedor, nome, endereco, online FROM Fornecedor LIMIT :limit OFFSET :offset";
            Query query = em.createNativeQuery(sql, Fornecedor.class);
            query.setParameter("limit", size);
            query.setParameter("offset", page * size);
            List<Fornecedor> list = query.getResultList();
            Logger.info("FornecedorDaoNativeImpl.findAll(page) - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Fornecedor> findByNome(String nome) {
        Logger.info("FornecedorDaoNativeImpl.findByNome - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_fornecedor, nome, endereco, online FROM Fornecedor WHERE nome=:nome";
            Query query = em.createNativeQuery(sql, Fornecedor.class);
            query.setParameter("nome", nome);
            List<Fornecedor> list = query.getResultList();
            Logger.info("FornecedorDaoNativeImpl.findByNome - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Fornecedor> findByFoto(byte[] foto) {
        Logger.info("FornecedorDaoNativeImpl.findByFoto - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_fornecedor, nome, foto, endereco, online FROM Fornecedor WHERE foto=:foto";
            Query query = em.createNativeQuery(sql, Fornecedor.class);
            query.setParameter("foto", foto);
            List<Fornecedor> list = query.getResultList();
            Logger.info("FornecedorDaoNativeImpl.findByFoto - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Fornecedor> findByEndereco(String endereco) {
        Logger.info("FornecedorDaoNativeImpl.findByEndereco - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_fornecedor, nome, endereco, online FROM Fornecedor WHERE endereco=:endereco";
            Query query = em.createNativeQuery(sql, Fornecedor.class);
            query.setParameter("endereco", endereco);
            List<Fornecedor> list = query.getResultList();
            Logger.info("FornecedorDaoNativeImpl.findByEndereco - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Fornecedor> findByOnline(Boolean online) {
        Logger.info("FornecedorDaoNativeImpl.findByOnline - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_fornecedor, nome, endereco, online FROM Fornecedor WHERE online=:online";
            Query query = em.createNativeQuery(sql, Fornecedor.class);
            query.setParameter("online", online);
            List<Fornecedor> list = query.getResultList();
            Logger.info("FornecedorDaoNativeImpl.findByOnline - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Fornecedor> search(Fornecedor filtro) {
        return search(filtro, -1, -1);
    }

    @Override
    public List<Fornecedor> search(Fornecedor filtro, int page, int size) {
        Logger.info("FornecedorDaoNativeImpl.search - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            StringBuilder sb = new StringBuilder("SELECT id_fornecedor, nome, endereco, online FROM Fornecedor WHERE 1=1");
            Map<String, Object> params = new HashMap<>();
            if (filtro.getNome() != null && !filtro.getNome().isEmpty()) {
                sb.append(" AND nome=:nome");
                params.put("nome", filtro.getNome());
            }
            if (filtro.getFoto() != null) {
                sb.append(" AND foto=:foto");
                params.put("foto", filtro.getFoto());
            }
            if (filtro.getEndereco() != null && !filtro.getEndereco().isEmpty()) {
                sb.append(" AND endereco=:endereco");
                params.put("endereco", filtro.getEndereco());
            }
            if (filtro.getOnline() != null) {
                sb.append(" AND online=:online");
                params.put("online", filtro.getOnline());
            }
            if (page >= 0 && size > 0) {
                sb.append(" LIMIT :limit OFFSET :offset");
            }
            Query query = em.createNativeQuery(sb.toString(), Fornecedor.class);
            for (Map.Entry<String, Object> e : params.entrySet()) {
                query.setParameter(e.getKey(), e.getValue());
            }
            if (page >= 0 && size > 0) {
                query.setParameter("limit", size);
                query.setParameter("offset", page * size);
            }
            List<Fornecedor> list = query.getResultList();
            Logger.info("FornecedorDaoNativeImpl.search - sucesso");
            return list;
        } finally {
            em.close();
        }
    }
}
