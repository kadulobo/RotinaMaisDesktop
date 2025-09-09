// path: src/main/java/dao/impl/CategoriaDaoNativeImpl.java
package dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dao.api.CategoriaDao;
import exception.CategoriaException;
import infra.EntityManagerUtil;
import infra.Logger;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import model.Categoria;

public class CategoriaDaoNativeImpl implements CategoriaDao {

    @Override
    public void create(Categoria categoria) throws CategoriaException {
        Logger.info("CategoriaDaoNativeImpl.create - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            String sql = "INSERT INTO Categoria (nome, descricao, foto, data_criacao) " +
                    "VALUES (:nome, :descricao, :foto, :dataCriacao)";
            Query query = em.createNativeQuery(sql);
            query.setParameter("nome", categoria.getNome());
            query.setParameter("descricao", categoria.getDescricao());
            query.setParameter("foto", categoria.getFoto());
            query.setParameter("dataCriacao", categoria.getDataCriacao());
            query.executeUpdate();
            em.getTransaction().commit();
            Logger.info("CategoriaDaoNativeImpl.create - sucesso");
        } catch (Exception e) {
            em.getTransaction().rollback();
            Logger.error("CategoriaDaoNativeImpl.create - erro", e);
            throw new CategoriaException("Erro ao criar Categoria", e);
        } finally {
            em.close();
        }
    }

    @Override
    public Categoria update(Categoria categoria) throws CategoriaException {
        Logger.info("CategoriaDaoNativeImpl.update - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            String sql = "UPDATE Categoria SET nome=:nome, descricao=:descricao, foto=:foto, data_criacao=:dataCriacao " +
                    "WHERE id_categoria=:id";
            Query query = em.createNativeQuery(sql);
            query.setParameter("nome", categoria.getNome());
            query.setParameter("descricao", categoria.getDescricao());
            query.setParameter("foto", categoria.getFoto());
            query.setParameter("dataCriacao", categoria.getDataCriacao());
            query.setParameter("id", categoria.getIdCategoria());
            int updated = query.executeUpdate();
            if (updated == 0) {
                throw new CategoriaException("Categoria não encontrada: id=" + categoria.getIdCategoria());
            }
            em.getTransaction().commit();
            Logger.info("CategoriaDaoNativeImpl.update - sucesso");
            return findById(categoria.getIdCategoria());
        } catch (CategoriaException e) {
            em.getTransaction().rollback();
            Logger.error("CategoriaDaoNativeImpl.update - erro", e);
            throw e;
        } catch (Exception e) {
            em.getTransaction().rollback();
            Logger.error("CategoriaDaoNativeImpl.update - erro", e);
            throw new CategoriaException("Erro ao atualizar Categoria", e);
        } finally {
            em.close();
        }
    }

    @Override
    public void deleteById(Integer id) throws CategoriaException {
        Logger.info("CategoriaDaoNativeImpl.deleteById - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            String sql = "DELETE FROM Categoria WHERE id_categoria=:id";
            Query query = em.createNativeQuery(sql);
            query.setParameter("id", id);
            int deleted = query.executeUpdate();
            if (deleted == 0) {
                throw new CategoriaException("Categoria não encontrada: id=" + id);
            }
            em.getTransaction().commit();
            Logger.info("CategoriaDaoNativeImpl.deleteById - sucesso");
        } catch (CategoriaException e) {
            em.getTransaction().rollback();
            Logger.error("CategoriaDaoNativeImpl.deleteById - erro", e);
            throw e;
        } catch (Exception e) {
            em.getTransaction().rollback();
            Logger.error("CategoriaDaoNativeImpl.deleteById - erro", e);
            throw new CategoriaException("Erro ao deletar Categoria", e);
        } finally {
            em.close();
        }
    }

    @Override
    public Categoria findById(Integer id) throws CategoriaException {
        Logger.info("CategoriaDaoNativeImpl.findById - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_categoria, nome, descricao, foto, data_criacao FROM Categoria WHERE id_categoria=:id";
            Query query = em.createNativeQuery(sql, Categoria.class);
            query.setParameter("id", id);
            Categoria c = (Categoria) query.getSingleResult();
            Logger.info("CategoriaDaoNativeImpl.findById - sucesso");
            return c;
        } catch (Exception e) {
            Logger.error("CategoriaDaoNativeImpl.findById - erro", e);
            throw new CategoriaException("Categoria não encontrada: id=" + id, e);
        } finally {
            em.close();
        }
    }

    @Override
    public Categoria findWithBlobsById(Integer id) throws CategoriaException {
        return findById(id);
    }

    @Override
    public List<Categoria> findAll() {
        Logger.info("CategoriaDaoNativeImpl.findAll - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_categoria, nome, descricao, foto, data_criacao FROM Categoria";
            Query query = em.createNativeQuery(sql, Categoria.class);
            List<Categoria> list = query.getResultList();
            Logger.info("CategoriaDaoNativeImpl.findAll - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Categoria> findAll(int page, int size) {
        Logger.info("CategoriaDaoNativeImpl.findAll(page) - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_categoria, nome, descricao, foto, data_criacao FROM Categoria LIMIT :limit OFFSET :offset";
            Query query = em.createNativeQuery(sql, Categoria.class);
            query.setParameter("limit", size);
            query.setParameter("offset", page * size);
            List<Categoria> list = query.getResultList();
            Logger.info("CategoriaDaoNativeImpl.findAll(page) - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Categoria> findByNome(String nome) {
        Logger.info("CategoriaDaoNativeImpl.findByNome - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_categoria, nome, descricao, foto, data_criacao FROM Categoria WHERE nome=:nome";
            Query query = em.createNativeQuery(sql, Categoria.class);
            query.setParameter("nome", nome);
            List<Categoria> list = query.getResultList();
            Logger.info("CategoriaDaoNativeImpl.findByNome - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Categoria> findByDescricao(String descricao) {
        Logger.info("CategoriaDaoNativeImpl.findByDescricao - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_categoria, nome, descricao, foto, data_criacao FROM Categoria WHERE descricao=:descricao";
            Query query = em.createNativeQuery(sql, Categoria.class);
            query.setParameter("descricao", descricao);
            List<Categoria> list = query.getResultList();
            Logger.info("CategoriaDaoNativeImpl.findByDescricao - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Categoria> findByDataCriacao(java.time.LocalDate dataCriacao) {
        Logger.info("CategoriaDaoNativeImpl.findByDataCriacao - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_categoria, nome, descricao, foto, data_criacao FROM Categoria WHERE data_criacao=:data";
            Query query = em.createNativeQuery(sql, Categoria.class);
            query.setParameter("data", dataCriacao);
            List<Categoria> list = query.getResultList();
            Logger.info("CategoriaDaoNativeImpl.findByDataCriacao - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Categoria> findByFoto(byte[] foto) {
        Logger.info("CategoriaDaoNativeImpl.findByFoto - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_categoria, nome, descricao, foto, data_criacao FROM Categoria WHERE foto=:foto";
            Query query = em.createNativeQuery(sql, Categoria.class);
            query.setParameter("foto", foto);
            List<Categoria> list = query.getResultList();
            Logger.info("CategoriaDaoNativeImpl.findByFoto - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Categoria> search(Categoria filtro) {
        return search(filtro, -1, -1);
    }

    @Override
    public List<Categoria> search(Categoria filtro, int page, int size) {
        Logger.info("CategoriaDaoNativeImpl.search - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            StringBuilder sb = new StringBuilder("SELECT id_categoria, nome, descricao, foto, data_criacao FROM Categoria WHERE 1=1");
            Map<String, Object> params = new HashMap<>();
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
            if (filtro.getFoto() != null) {
                sb.append(" AND foto=:foto");
                params.put("foto", filtro.getFoto());
            }
            if (page >= 0 && size > 0) {
                sb.append(" LIMIT :limit OFFSET :offset");
            }
            Query query = em.createNativeQuery(sb.toString(), Categoria.class);
            for (Map.Entry<String, Object> e : params.entrySet()) {
                query.setParameter(e.getKey(), e.getValue());
            }
            if (page >= 0 && size > 0) {
                query.setParameter("limit", size);
                query.setParameter("offset", page * size);
            }
            List<Categoria> list = query.getResultList();
            Logger.info("CategoriaDaoNativeImpl.search - sucesso");
            return list;
        } finally {
            em.close();
        }
    }
}
