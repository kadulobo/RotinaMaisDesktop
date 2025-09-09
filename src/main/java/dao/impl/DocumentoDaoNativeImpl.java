// path: src/main/java/dao/impl/DocumentoDaoNativeImpl.java
package dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dao.api.DocumentoDao;
import exception.DocumentoException;
import infra.EntityManagerUtil;
import infra.Logger;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import model.Documento;

public class DocumentoDaoNativeImpl implements DocumentoDao {

    @Override
    public void create(Documento documento) throws DocumentoException {
        Logger.info("DocumentoDaoNativeImpl.create - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            String sql = "INSERT INTO Documento (nome, arquivo, foto, video, data, id_usuario) " +
                    "VALUES (:nome, :arquivo, :foto, :video, :data, :idUsuario)";
            Query query = em.createNativeQuery(sql);
            query.setParameter("nome", documento.getNome());
            query.setParameter("arquivo", documento.getArquivo());
            query.setParameter("foto", documento.getFoto());
            query.setParameter("video", documento.getVideo());
            query.setParameter("data", documento.getData());
            query.setParameter("idUsuario", documento.getIdUsuario());
            query.executeUpdate();
            em.getTransaction().commit();
            Logger.info("DocumentoDaoNativeImpl.create - sucesso");
        } catch (Exception e) {
            em.getTransaction().rollback();
            Logger.error("DocumentoDaoNativeImpl.create - erro", e);
            throw new DocumentoException("Erro ao criar Documento", e);
        } finally {
            em.close();
        }
    }

    @Override
    public Documento update(Documento documento) throws DocumentoException {
        Logger.info("DocumentoDaoNativeImpl.update - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            String sql = "UPDATE Documento SET nome=:nome, arquivo=:arquivo, foto=:foto, video=:video, data=:data, id_usuario=:idUsuario WHERE id_documento=:id";
            Query query = em.createNativeQuery(sql);
            query.setParameter("nome", documento.getNome());
            query.setParameter("arquivo", documento.getArquivo());
            query.setParameter("foto", documento.getFoto());
            query.setParameter("video", documento.getVideo());
            query.setParameter("data", documento.getData());
            query.setParameter("idUsuario", documento.getIdUsuario());
            query.setParameter("id", documento.getIdDocumento());
            int updated = query.executeUpdate();
            if (updated == 0) {
                throw new DocumentoException("Documento não encontrado: id=" + documento.getIdDocumento());
            }
            em.getTransaction().commit();
            Logger.info("DocumentoDaoNativeImpl.update - sucesso");
            return findById(documento.getIdDocumento());
        } catch (DocumentoException e) {
            em.getTransaction().rollback();
            Logger.error("DocumentoDaoNativeImpl.update - erro", e);
            throw e;
        } catch (Exception e) {
            em.getTransaction().rollback();
            Logger.error("DocumentoDaoNativeImpl.update - erro", e);
            throw new DocumentoException("Erro ao atualizar Documento", e);
        } finally {
            em.close();
        }
    }

    @Override
    public void deleteById(Integer id) throws DocumentoException {
        Logger.info("DocumentoDaoNativeImpl.deleteById - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            String sql = "DELETE FROM Documento WHERE id_documento=:id";
            Query query = em.createNativeQuery(sql);
            query.setParameter("id", id);
            int deleted = query.executeUpdate();
            if (deleted == 0) {
                throw new DocumentoException("Documento não encontrado: id=" + id);
            }
            em.getTransaction().commit();
            Logger.info("DocumentoDaoNativeImpl.deleteById - sucesso");
        } catch (DocumentoException e) {
            em.getTransaction().rollback();
            Logger.error("DocumentoDaoNativeImpl.deleteById - erro", e);
            throw e;
        } catch (Exception e) {
            em.getTransaction().rollback();
            Logger.error("DocumentoDaoNativeImpl.deleteById - erro", e);
            throw new DocumentoException("Erro ao deletar Documento", e);
        } finally {
            em.close();
        }
    }

    @Override
    public Documento findById(Integer id) throws DocumentoException {
        Logger.info("DocumentoDaoNativeImpl.findById - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_documento, nome, data, id_usuario FROM Documento WHERE id_documento=:id";
            Query query = em.createNativeQuery(sql, Documento.class);
            query.setParameter("id", id);
            Documento d = (Documento) query.getSingleResult();
            Logger.info("DocumentoDaoNativeImpl.findById - sucesso");
            return d;
        } catch (Exception e) {
            Logger.error("DocumentoDaoNativeImpl.findById - erro", e);
            throw new DocumentoException("Documento não encontrado: id=" + id, e);
        } finally {
            em.close();
        }
    }

    @Override
    public Documento findWithBlobsById(Integer id) throws DocumentoException {
        Logger.info("DocumentoDaoNativeImpl.findWithBlobsById - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_documento, nome, arquivo, foto, video, data, id_usuario FROM Documento WHERE id_documento=:id";
            Query query = em.createNativeQuery(sql, Documento.class);
            query.setParameter("id", id);
            Documento d = (Documento) query.getSingleResult();
            Logger.info("DocumentoDaoNativeImpl.findWithBlobsById - sucesso");
            return d;
        } catch (Exception e) {
            Logger.error("DocumentoDaoNativeImpl.findWithBlobsById - erro", e);
            throw new DocumentoException("Documento não encontrado: id=" + id, e);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Documento> findAll() {
        Logger.info("DocumentoDaoNativeImpl.findAll - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_documento, nome, data, id_usuario FROM Documento";
            Query query = em.createNativeQuery(sql, Documento.class);
            List<Documento> list = query.getResultList();
            Logger.info("DocumentoDaoNativeImpl.findAll - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Documento> findAll(int page, int size) {
        Logger.info("DocumentoDaoNativeImpl.findAll(page) - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_documento, nome, data, id_usuario FROM Documento LIMIT :limit OFFSET :offset";
            Query query = em.createNativeQuery(sql, Documento.class);
            query.setParameter("limit", size);
            query.setParameter("offset", page * size);
            List<Documento> list = query.getResultList();
            Logger.info("DocumentoDaoNativeImpl.findAll(page) - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Documento> findByNome(String nome) {
        Logger.info("DocumentoDaoNativeImpl.findByNome - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_documento, nome, data, id_usuario FROM Documento WHERE nome=:nome";
            Query query = em.createNativeQuery(sql, Documento.class);
            query.setParameter("nome", nome);
            List<Documento> list = query.getResultList();
            Logger.info("DocumentoDaoNativeImpl.findByNome - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Documento> findByArquivo(byte[] arquivo) {
        Logger.info("DocumentoDaoNativeImpl.findByArquivo - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_documento, nome, arquivo, foto, video, data, id_usuario FROM Documento WHERE arquivo=:arquivo";
            Query query = em.createNativeQuery(sql, Documento.class);
            query.setParameter("arquivo", arquivo);
            List<Documento> list = query.getResultList();
            Logger.info("DocumentoDaoNativeImpl.findByArquivo - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Documento> findByFoto(byte[] foto) {
        Logger.info("DocumentoDaoNativeImpl.findByFoto - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_documento, nome, arquivo, foto, video, data, id_usuario FROM Documento WHERE foto=:foto";
            Query query = em.createNativeQuery(sql, Documento.class);
            query.setParameter("foto", foto);
            List<Documento> list = query.getResultList();
            Logger.info("DocumentoDaoNativeImpl.findByFoto - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Documento> findByVideo(byte[] video) {
        Logger.info("DocumentoDaoNativeImpl.findByVideo - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_documento, nome, arquivo, foto, video, data, id_usuario FROM Documento WHERE video=:video";
            Query query = em.createNativeQuery(sql, Documento.class);
            query.setParameter("video", video);
            List<Documento> list = query.getResultList();
            Logger.info("DocumentoDaoNativeImpl.findByVideo - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Documento> findByData(java.time.LocalDate data) {
        Logger.info("DocumentoDaoNativeImpl.findByData - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_documento, nome, data, id_usuario FROM Documento WHERE data=:data";
            Query query = em.createNativeQuery(sql, Documento.class);
            query.setParameter("data", data);
            List<Documento> list = query.getResultList();
            Logger.info("DocumentoDaoNativeImpl.findByData - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Documento> findByIdUsuario(Integer idUsuario) {
        Logger.info("DocumentoDaoNativeImpl.findByIdUsuario - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_documento, nome, data, id_usuario FROM Documento WHERE id_usuario=:idUsuario";
            Query query = em.createNativeQuery(sql, Documento.class);
            query.setParameter("idUsuario", idUsuario);
            List<Documento> list = query.getResultList();
            Logger.info("DocumentoDaoNativeImpl.findByIdUsuario - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Documento> search(Documento filtro) {
        return search(filtro, -1, -1);
    }

    @Override
    public List<Documento> search(Documento filtro, int page, int size) {
        Logger.info("DocumentoDaoNativeImpl.search - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            StringBuilder sb = new StringBuilder("SELECT id_documento, nome, data, id_usuario FROM Documento WHERE 1=1");
            Map<String, Object> params = new HashMap<>();
            if (filtro.getNome() != null && !filtro.getNome().isEmpty()) {
                sb.append(" AND nome=:nome");
                params.put("nome", filtro.getNome());
            }
            if (filtro.getArquivo() != null) {
                sb.append(" AND arquivo=:arquivo");
                params.put("arquivo", filtro.getArquivo());
            }
            if (filtro.getFoto() != null) {
                sb.append(" AND foto=:foto");
                params.put("foto", filtro.getFoto());
            }
            if (filtro.getVideo() != null) {
                sb.append(" AND video=:video");
                params.put("video", filtro.getVideo());
            }
            if (filtro.getData() != null) {
                sb.append(" AND data=:data");
                params.put("data", filtro.getData());
            }
            if (filtro.getIdUsuario() != null) {
                sb.append(" AND id_usuario=:idUsuario");
                params.put("idUsuario", filtro.getIdUsuario());
            }
            if (page >= 0 && size > 0) {
                sb.append(" LIMIT :limit OFFSET :offset");
            }
            Query query = em.createNativeQuery(sb.toString(), Documento.class);
            for (Map.Entry<String, Object> e : params.entrySet()) {
                query.setParameter(e.getKey(), e.getValue());
            }
            if (page >= 0 && size > 0) {
                query.setParameter("limit", size);
                query.setParameter("offset", page * size);
            }
            List<Documento> list = query.getResultList();
            Logger.info("DocumentoDaoNativeImpl.search - sucesso");
            return list;
        } finally {
            em.close();
        }
    }
}
