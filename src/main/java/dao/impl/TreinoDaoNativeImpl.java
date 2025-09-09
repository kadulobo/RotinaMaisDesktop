// path: src/main/java/dao/impl/TreinoDaoNativeImpl.java
package dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dao.api.TreinoDao;
import exception.TreinoException;
import infra.EntityManagerUtil;
import infra.Logger;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import model.Treino;

public class TreinoDaoNativeImpl implements TreinoDao {

    @Override
    public void create(Treino treino) throws TreinoException {
        Logger.info("TreinoDaoNativeImpl.create - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            String sql = "INSERT INTO Treino (nome, classe, id_rotina) VALUES (:nome, :classe, CAST(:idRotina AS INTEGER))";
            Query query = em.createNativeQuery(sql);
            query.setParameter("nome", treino.getNome());
            query.setParameter("classe", treino.getClasse());
            query.setParameter("idRotina", treino.getIdRotina());
            query.executeUpdate();
            em.getTransaction().commit();
            Logger.info("TreinoDaoNativeImpl.create - sucesso");
        } catch (Exception e) {
            em.getTransaction().rollback();
            Logger.error("TreinoDaoNativeImpl.create - erro", e);
            throw new TreinoException("Erro ao criar Treino", e);
        } finally {
            em.close();
        }
    }

    @Override
    public Treino update(Treino treino) throws TreinoException {
        Logger.info("TreinoDaoNativeImpl.update - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            String sql = "UPDATE Treino SET nome=:nome, classe=:classe, id_rotina=CAST(:idRotina AS INTEGER) WHERE id_treino=:id";
            Query query = em.createNativeQuery(sql);
            query.setParameter("nome", treino.getNome());
            query.setParameter("classe", treino.getClasse());
            query.setParameter("idRotina", treino.getIdRotina());
            query.setParameter("id", treino.getIdTreino());
            int updated = query.executeUpdate();
            if (updated == 0) {
                throw new TreinoException("Treino não encontrado: id=" + treino.getIdTreino());
            }
            em.getTransaction().commit();
            Logger.info("TreinoDaoNativeImpl.update - sucesso");
            return findById(treino.getIdTreino());
        } catch (TreinoException e) {
            em.getTransaction().rollback();
            Logger.error("TreinoDaoNativeImpl.update - erro", e);
            throw e;
        } catch (Exception e) {
            em.getTransaction().rollback();
            Logger.error("TreinoDaoNativeImpl.update - erro", e);
            throw new TreinoException("Erro ao atualizar Treino", e);
        } finally {
            em.close();
        }
    }

    @Override
    public void deleteById(Integer id) throws TreinoException {
        Logger.info("TreinoDaoNativeImpl.deleteById - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            String sql = "DELETE FROM Treino WHERE id_treino=:id";
            Query query = em.createNativeQuery(sql);
            query.setParameter("id", id);
            int deleted = query.executeUpdate();
            if (deleted == 0) {
                throw new TreinoException("Treino não encontrado: id=" + id);
            }
            em.getTransaction().commit();
            Logger.info("TreinoDaoNativeImpl.deleteById - sucesso");
        } catch (TreinoException e) {
            em.getTransaction().rollback();
            Logger.error("TreinoDaoNativeImpl.deleteById - erro", e);
            throw e;
        } catch (Exception e) {
            em.getTransaction().rollback();
            Logger.error("TreinoDaoNativeImpl.deleteById - erro", e);
            throw new TreinoException("Erro ao deletar Treino", e);
        } finally {
            em.close();
        }
    }

    @Override
    public Treino findById(Integer id) throws TreinoException {
        Logger.info("TreinoDaoNativeImpl.findById - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_treino, nome, classe, id_rotina FROM Treino WHERE id_treino=:id";
            Query query = em.createNativeQuery(sql, Treino.class);
            query.setParameter("id", id);
            Treino t = (Treino) query.getSingleResult();
            Logger.info("TreinoDaoNativeImpl.findById - sucesso");
            return t;
        } catch (Exception e) {
            Logger.error("TreinoDaoNativeImpl.findById - erro", e);
            throw new TreinoException("Treino não encontrado: id=" + id, e);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Treino> findAll() {
        Logger.info("TreinoDaoNativeImpl.findAll - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_treino, nome, classe, id_rotina FROM Treino";
            Query query = em.createNativeQuery(sql, Treino.class);
            List<Treino> list = query.getResultList();
            Logger.info("TreinoDaoNativeImpl.findAll - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Treino> findAll(int page, int size) {
        Logger.info("TreinoDaoNativeImpl.findAll(page) - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_treino, nome, classe, id_rotina FROM Treino LIMIT :limit OFFSET :offset";
            Query query = em.createNativeQuery(sql, Treino.class);
            query.setParameter("limit", size);
            query.setParameter("offset", page * size);
            List<Treino> list = query.getResultList();
            Logger.info("TreinoDaoNativeImpl.findAll(page) - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Treino> findByNome(String nome) {
        Logger.info("TreinoDaoNativeImpl.findByNome - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_treino, nome, classe, id_rotina FROM Treino WHERE nome=:nome";
            Query query = em.createNativeQuery(sql, Treino.class);
            query.setParameter("nome", nome);
            List<Treino> list = query.getResultList();
            Logger.info("TreinoDaoNativeImpl.findByNome - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Treino> findByClasse(String classe) {
        Logger.info("TreinoDaoNativeImpl.findByClasse - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_treino, nome, classe, id_rotina FROM Treino WHERE classe=:classe";
            Query query = em.createNativeQuery(sql, Treino.class);
            query.setParameter("classe", classe);
            List<Treino> list = query.getResultList();
            Logger.info("TreinoDaoNativeImpl.findByClasse - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Treino> findByIdRotina(Integer idRotina) {
        Logger.info("TreinoDaoNativeImpl.findByIdRotina - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_treino, nome, classe, id_rotina FROM Treino WHERE id_rotina=:idRotina";
            Query query = em.createNativeQuery(sql, Treino.class);
            query.setParameter("idRotina", idRotina);
            List<Treino> list = query.getResultList();
            Logger.info("TreinoDaoNativeImpl.findByIdRotina - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Treino> search(Treino filtro) {
        return search(filtro, -1, -1);
    }

    @Override
    public List<Treino> search(Treino filtro, int page, int size) {
        Logger.info("TreinoDaoNativeImpl.search - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            StringBuilder sb = new StringBuilder("SELECT id_treino, nome, classe, id_rotina FROM Treino WHERE 1=1");
            Map<String, Object> params = new HashMap<>();
            if (filtro.getNome() != null && !filtro.getNome().isEmpty()) {
                sb.append(" AND nome=:nome");
                params.put("nome", filtro.getNome());
            }
            if (filtro.getClasse() != null && !filtro.getClasse().isEmpty()) {
                sb.append(" AND classe=:classe");
                params.put("classe", filtro.getClasse());
            }
            if (filtro.getIdRotina() != null) {
                sb.append(" AND id_rotina=:idRotina");
                params.put("idRotina", filtro.getIdRotina());
            }
            if (page >= 0 && size > 0) {
                sb.append(" LIMIT :limit OFFSET :offset");
            }
            Query query = em.createNativeQuery(sb.toString(), Treino.class);
            for (Map.Entry<String, Object> e : params.entrySet()) {
                query.setParameter(e.getKey(), e.getValue());
            }
            if (page >= 0 && size > 0) {
                query.setParameter("limit", size);
                query.setParameter("offset", page * size);
            }
            List<Treino> list = query.getResultList();
            Logger.info("TreinoDaoNativeImpl.search - sucesso");
            return list;
        } finally {
            em.close();
        }
    }
}
