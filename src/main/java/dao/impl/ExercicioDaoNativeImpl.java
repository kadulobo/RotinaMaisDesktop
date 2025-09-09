// path: src/main/java/dao/impl/ExercicioDaoNativeImpl.java
package dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dao.api.ExercicioDao;
import exception.ExercicioException;
import infra.EntityManagerUtil;
import infra.Logger;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import model.Exercicio;

public class ExercicioDaoNativeImpl implements ExercicioDao {

    @Override
    public void create(Exercicio exercicio) throws ExercicioException {
        Logger.info("ExercicioDaoNativeImpl.create - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            String sql = "INSERT INTO Exercicio (id_exercicio, nome, carga_leve, carga_media, carga_maxima) " +
                    "VALUES (:id, :nome, :cargaLeve, :cargaMedia, :cargaMaxima)";
            Query query = em.createNativeQuery(sql);
            query.setParameter("id", exercicio.getIdExercicio());
            query.setParameter("nome", exercicio.getNome());
            query.setParameter("cargaLeve", exercicio.getCargaLeve());
            query.setParameter("cargaMedia", exercicio.getCargaMedia());
            query.setParameter("cargaMaxima", exercicio.getCargaMaxima());
            query.executeUpdate();
            em.getTransaction().commit();
            Logger.info("ExercicioDaoNativeImpl.create - sucesso");
        } catch (Exception e) {
            em.getTransaction().rollback();
            Logger.error("ExercicioDaoNativeImpl.create - erro", e);
            throw new ExercicioException("Erro ao criar Exercicio", e);
        } finally {
            em.close();
        }
    }

    @Override
    public Exercicio update(Exercicio exercicio) throws ExercicioException {
        Logger.info("ExercicioDaoNativeImpl.update - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            String sql = "UPDATE Exercicio SET nome=:nome, carga_leve=:cargaLeve, carga_media=:cargaMedia, carga_maxima=:cargaMaxima WHERE id_exercicio=:id";
            Query query = em.createNativeQuery(sql);
            query.setParameter("nome", exercicio.getNome());
            query.setParameter("cargaLeve", exercicio.getCargaLeve());
            query.setParameter("cargaMedia", exercicio.getCargaMedia());
            query.setParameter("cargaMaxima", exercicio.getCargaMaxima());
            query.setParameter("id", exercicio.getIdExercicio());
            int updated = query.executeUpdate();
            if (updated == 0) {
                throw new ExercicioException("Exercicio não encontrado: id=" + exercicio.getIdExercicio());
            }
            em.getTransaction().commit();
            Logger.info("ExercicioDaoNativeImpl.update - sucesso");
            return findById(exercicio.getIdExercicio());
        } catch (ExercicioException e) {
            em.getTransaction().rollback();
            Logger.error("ExercicioDaoNativeImpl.update - erro", e);
            throw e;
        } catch (Exception e) {
            em.getTransaction().rollback();
            Logger.error("ExercicioDaoNativeImpl.update - erro", e);
            throw new ExercicioException("Erro ao atualizar Exercicio", e);
        } finally {
            em.close();
        }
    }

    @Override
    public void deleteById(Integer id) throws ExercicioException {
        Logger.info("ExercicioDaoNativeImpl.deleteById - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            String sql = "DELETE FROM Exercicio WHERE id_exercicio=:id";
            Query query = em.createNativeQuery(sql);
            query.setParameter("id", id);
            int deleted = query.executeUpdate();
            if (deleted == 0) {
                throw new ExercicioException("Exercicio não encontrado: id=" + id);
            }
            em.getTransaction().commit();
            Logger.info("ExercicioDaoNativeImpl.deleteById - sucesso");
        } catch (ExercicioException e) {
            em.getTransaction().rollback();
            Logger.error("ExercicioDaoNativeImpl.deleteById - erro", e);
            throw e;
        } catch (Exception e) {
            em.getTransaction().rollback();
            Logger.error("ExercicioDaoNativeImpl.deleteById - erro", e);
            throw new ExercicioException("Erro ao deletar Exercicio", e);
        } finally {
            em.close();
        }
    }

    @Override
    public Exercicio findById(Integer id) throws ExercicioException {
        Logger.info("ExercicioDaoNativeImpl.findById - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_exercicio, nome, carga_leve, carga_media, carga_maxima FROM Exercicio WHERE id_exercicio=:id";
            Query query = em.createNativeQuery(sql, Exercicio.class);
            query.setParameter("id", id);
            Exercicio e = (Exercicio) query.getSingleResult();
            Logger.info("ExercicioDaoNativeImpl.findById - sucesso");
            return e;
        } catch (Exception e) {
            Logger.error("ExercicioDaoNativeImpl.findById - erro", e);
            throw new ExercicioException("Exercicio não encontrado: id=" + id, e);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Exercicio> findAll() {
        Logger.info("ExercicioDaoNativeImpl.findAll - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_exercicio, nome, carga_leve, carga_media, carga_maxima FROM Exercicio";
            Query query = em.createNativeQuery(sql, Exercicio.class);
            List<Exercicio> list = query.getResultList();
            Logger.info("ExercicioDaoNativeImpl.findAll - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Exercicio> findAll(int page, int size) {
        Logger.info("ExercicioDaoNativeImpl.findAll(page) - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_exercicio, nome, carga_leve, carga_media, carga_maxima FROM Exercicio LIMIT :limit OFFSET :offset";
            Query query = em.createNativeQuery(sql, Exercicio.class);
            query.setParameter("limit", size);
            query.setParameter("offset", page * size);
            List<Exercicio> list = query.getResultList();
            Logger.info("ExercicioDaoNativeImpl.findAll(page) - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Exercicio> findByNome(String nome) {
        Logger.info("ExercicioDaoNativeImpl.findByNome - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_exercicio, nome, carga_leve, carga_media, carga_maxima FROM Exercicio WHERE nome=:nome";
            Query query = em.createNativeQuery(sql, Exercicio.class);
            query.setParameter("nome", nome);
            List<Exercicio> list = query.getResultList();
            Logger.info("ExercicioDaoNativeImpl.findByNome - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Exercicio> findByCargaLeve(Integer cargaLeve) {
        Logger.info("ExercicioDaoNativeImpl.findByCargaLeve - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_exercicio, nome, carga_leve, carga_media, carga_maxima FROM Exercicio WHERE carga_leve=:cargaLeve";
            Query query = em.createNativeQuery(sql, Exercicio.class);
            query.setParameter("cargaLeve", cargaLeve);
            List<Exercicio> list = query.getResultList();
            Logger.info("ExercicioDaoNativeImpl.findByCargaLeve - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Exercicio> findByCargaMedia(Integer cargaMedia) {
        Logger.info("ExercicioDaoNativeImpl.findByCargaMedia - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_exercicio, nome, carga_leve, carga_media, carga_maxima FROM Exercicio WHERE carga_media=:cargaMedia";
            Query query = em.createNativeQuery(sql, Exercicio.class);
            query.setParameter("cargaMedia", cargaMedia);
            List<Exercicio> list = query.getResultList();
            Logger.info("ExercicioDaoNativeImpl.findByCargaMedia - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Exercicio> findByCargaMaxima(Integer cargaMaxima) {
        Logger.info("ExercicioDaoNativeImpl.findByCargaMaxima - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_exercicio, nome, carga_leve, carga_media, carga_maxima FROM Exercicio WHERE carga_maxima=:cargaMaxima";
            Query query = em.createNativeQuery(sql, Exercicio.class);
            query.setParameter("cargaMaxima", cargaMaxima);
            List<Exercicio> list = query.getResultList();
            Logger.info("ExercicioDaoNativeImpl.findByCargaMaxima - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Exercicio> search(Exercicio filtro) {
        return search(filtro, -1, -1);
    }

    @Override
    public List<Exercicio> search(Exercicio filtro, int page, int size) {
        Logger.info("ExercicioDaoNativeImpl.search - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            StringBuilder sb = new StringBuilder("SELECT id_exercicio, nome, carga_leve, carga_media, carga_maxima FROM Exercicio WHERE 1=1");
            Map<String, Object> params = new HashMap<>();
            if (filtro.getNome() != null && !filtro.getNome().isEmpty()) {
                sb.append(" AND nome=:nome");
                params.put("nome", filtro.getNome());
            }
            if (filtro.getCargaLeve() != null) {
                sb.append(" AND carga_leve=:cargaLeve");
                params.put("cargaLeve", filtro.getCargaLeve());
            }
            if (filtro.getCargaMedia() != null) {
                sb.append(" AND carga_media=:cargaMedia");
                params.put("cargaMedia", filtro.getCargaMedia());
            }
            if (filtro.getCargaMaxima() != null) {
                sb.append(" AND carga_maxima=:cargaMaxima");
                params.put("cargaMaxima", filtro.getCargaMaxima());
            }
            if (page >= 0 && size > 0) {
                sb.append(" LIMIT :limit OFFSET :offset");
            }
            Query query = em.createNativeQuery(sb.toString(), Exercicio.class);
            for (Map.Entry<String, Object> e : params.entrySet()) {
                query.setParameter(e.getKey(), e.getValue());
            }
            if (page >= 0 && size > 0) {
                query.setParameter("limit", size);
                query.setParameter("offset", page * size);
            }
            List<Exercicio> list = query.getResultList();
            Logger.info("ExercicioDaoNativeImpl.search - sucesso");
            return list;
        } finally {
            em.close();
        }
    }
}
