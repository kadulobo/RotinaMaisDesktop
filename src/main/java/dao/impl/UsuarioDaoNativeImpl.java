// path: src/main/java/dao/impl/UsuarioDaoNativeImpl.java
package dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dao.api.UsuarioDao;
import exception.UsuarioException;
import infra.EntityManagerUtil;
import infra.Logger;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import model.Usuario;

public class UsuarioDaoNativeImpl implements UsuarioDao {

    @Override
    public void create(Usuario usuario) throws UsuarioException {
        Logger.info("UsuarioDaoNativeImpl.create - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            String sql = "INSERT INTO Usuario (id_usuario, nome, senha, foto, email) " +
                    "VALUES (:id, :nome, :senha, :foto, :email)";
            Query query = em.createNativeQuery(sql);
            query.setParameter("id", usuario.getIdUsuario());
            query.setParameter("nome", usuario.getNome());
            query.setParameter("senha", usuario.getSenha());
            query.setParameter("foto", usuario.getFoto());
            query.setParameter("email", usuario.getEmail());
            query.executeUpdate();
            em.getTransaction().commit();
            Logger.info("UsuarioDaoNativeImpl.create - sucesso");
        } catch (Exception e) {
            em.getTransaction().rollback();
            Logger.error("UsuarioDaoNativeImpl.create - erro", e);
            throw new UsuarioException("Erro ao criar Usuario", e);
        } finally {
            em.close();
        }
    }

    @Override
    public Usuario update(Usuario usuario) throws UsuarioException {
        Logger.info("UsuarioDaoNativeImpl.update - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            String sql = "UPDATE Usuario SET nome=:nome, senha=:senha, foto=:foto, email=:email WHERE id_usuario=:id";
            Query query = em.createNativeQuery(sql);
            query.setParameter("nome", usuario.getNome());
            query.setParameter("senha", usuario.getSenha());
            query.setParameter("foto", usuario.getFoto());
            query.setParameter("email", usuario.getEmail());
            query.setParameter("id", usuario.getIdUsuario());
            int updated = query.executeUpdate();
            if (updated == 0) {
                throw new UsuarioException("Usuario não encontrado: id=" + usuario.getIdUsuario());
            }
            em.getTransaction().commit();
            Logger.info("UsuarioDaoNativeImpl.update - sucesso");
            return findById(usuario.getIdUsuario());
        } catch (UsuarioException e) {
            em.getTransaction().rollback();
            Logger.error("UsuarioDaoNativeImpl.update - erro", e);
            throw e;
        } catch (Exception e) {
            em.getTransaction().rollback();
            Logger.error("UsuarioDaoNativeImpl.update - erro", e);
            throw new UsuarioException("Erro ao atualizar Usuario", e);
        } finally {
            em.close();
        }
    }

    @Override
    public void deleteById(Integer id) throws UsuarioException {
        Logger.info("UsuarioDaoNativeImpl.deleteById - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            String sql = "DELETE FROM Usuario WHERE id_usuario=:id";
            Query query = em.createNativeQuery(sql);
            query.setParameter("id", id);
            int deleted = query.executeUpdate();
            if (deleted == 0) {
                throw new UsuarioException("Usuario não encontrado: id=" + id);
            }
            em.getTransaction().commit();
            Logger.info("UsuarioDaoNativeImpl.deleteById - sucesso");
        } catch (UsuarioException e) {
            em.getTransaction().rollback();
            Logger.error("UsuarioDaoNativeImpl.deleteById - erro", e);
            throw e;
        } catch (Exception e) {
            em.getTransaction().rollback();
            Logger.error("UsuarioDaoNativeImpl.deleteById - erro", e);
            throw new UsuarioException("Erro ao deletar Usuario", e);
        } finally {
            em.close();
        }
    }

    @Override
    public Usuario findById(Integer id) throws UsuarioException {
        Logger.info("UsuarioDaoNativeImpl.findById - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_usuario, nome, senha, email FROM Usuario WHERE id_usuario=:id";
            Query query = em.createNativeQuery(sql, Usuario.class);
            query.setParameter("id", id);
            Usuario u = (Usuario) query.getSingleResult();
            Logger.info("UsuarioDaoNativeImpl.findById - sucesso");
            return u;
        } catch (Exception e) {
            Logger.error("UsuarioDaoNativeImpl.findById - erro", e);
            throw new UsuarioException("Usuario não encontrado: id=" + id, e);
        } finally {
            em.close();
        }
    }

    @Override
    public Usuario findWithBlobsById(Integer id) throws UsuarioException {
        Logger.info("UsuarioDaoNativeImpl.findWithBlobsById - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_usuario, nome, senha, foto, email FROM Usuario WHERE id_usuario=:id";
            Query query = em.createNativeQuery(sql, Usuario.class);
            query.setParameter("id", id);
            Usuario u = (Usuario) query.getSingleResult();
            Logger.info("UsuarioDaoNativeImpl.findWithBlobsById - sucesso");
            return u;
        } catch (Exception e) {
            Logger.error("UsuarioDaoNativeImpl.findWithBlobsById - erro", e);
            throw new UsuarioException("Usuario não encontrado: id=" + id, e);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Usuario> findAll() {
        Logger.info("UsuarioDaoNativeImpl.findAll - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_usuario, nome, senha, email FROM Usuario";
            Query query = em.createNativeQuery(sql, Usuario.class);
            List<Usuario> list = query.getResultList();
            Logger.info("UsuarioDaoNativeImpl.findAll - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Usuario> findAll(int page, int size) {
        Logger.info("UsuarioDaoNativeImpl.findAll(page) - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_usuario, nome, senha, email FROM Usuario LIMIT :limit OFFSET :offset";
            Query query = em.createNativeQuery(sql, Usuario.class);
            query.setParameter("limit", size);
            query.setParameter("offset", page * size);
            List<Usuario> list = query.getResultList();
            Logger.info("UsuarioDaoNativeImpl.findAll(page) - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Usuario> findByNome(String nome) {
        Logger.info("UsuarioDaoNativeImpl.findByNome - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_usuario, nome, senha, email FROM Usuario WHERE nome=:nome";
            Query query = em.createNativeQuery(sql, Usuario.class);
            query.setParameter("nome", nome);
            List<Usuario> list = query.getResultList();
            Logger.info("UsuarioDaoNativeImpl.findByNome - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Usuario> findBySenha(String senha) {
        Logger.info("UsuarioDaoNativeImpl.findBySenha - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_usuario, nome, senha, email FROM Usuario WHERE senha=:senha";
            Query query = em.createNativeQuery(sql, Usuario.class);
            query.setParameter("senha", senha);
            List<Usuario> list = query.getResultList();
            Logger.info("UsuarioDaoNativeImpl.findBySenha - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Usuario> findByEmail(String email) {
        Logger.info("UsuarioDaoNativeImpl.findByEmail - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_usuario, nome, senha, email FROM Usuario WHERE email=:email";
            Query query = em.createNativeQuery(sql, Usuario.class);
            query.setParameter("email", email);
            List<Usuario> list = query.getResultList();
            Logger.info("UsuarioDaoNativeImpl.findByEmail - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Usuario> findByFoto(byte[] foto) {
        Logger.info("UsuarioDaoNativeImpl.findByFoto - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_usuario, nome, senha, foto, email FROM Usuario WHERE foto=:foto";
            Query query = em.createNativeQuery(sql, Usuario.class);
            query.setParameter("foto", foto);
            List<Usuario> list = query.getResultList();
            Logger.info("UsuarioDaoNativeImpl.findByFoto - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Usuario> search(Usuario filtro) {
        return search(filtro, -1, -1);
    }

    @Override
    public List<Usuario> search(Usuario filtro, int page, int size) {
        Logger.info("UsuarioDaoNativeImpl.search - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            StringBuilder sb = new StringBuilder("SELECT id_usuario, nome, senha, email FROM Usuario WHERE 1=1");
            Map<String, Object> params = new HashMap<>();
            if (filtro.getNome() != null && !filtro.getNome().isEmpty()) {
                sb.append(" AND nome=:nome");
                params.put("nome", filtro.getNome());
            }
            if (filtro.getSenha() != null && !filtro.getSenha().isEmpty()) {
                sb.append(" AND senha=:senha");
                params.put("senha", filtro.getSenha());
            }
            if (filtro.getEmail() != null && !filtro.getEmail().isEmpty()) {
                sb.append(" AND email=:email");
                params.put("email", filtro.getEmail());
            }
            if (page >= 0 && size > 0) {
                sb.append(" LIMIT :limit OFFSET :offset");
            }
            Query query = em.createNativeQuery(sb.toString(), Usuario.class);
            for (Map.Entry<String, Object> e : params.entrySet()) {
                query.setParameter(e.getKey(), e.getValue());
            }
            if (page >= 0 && size > 0) {
                query.setParameter("limit", size);
                query.setParameter("offset", page * size);
            }
            List<Usuario> list = query.getResultList();
            Logger.info("UsuarioDaoNativeImpl.search - sucesso");
            return list;
        } finally {
            em.close();
        }
    }
}
