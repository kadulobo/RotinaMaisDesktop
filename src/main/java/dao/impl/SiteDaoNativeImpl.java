// path: src/main/java/dao/impl/SiteDaoNativeImpl.java
package dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dao.api.SiteDao;
import exception.SiteException;
import infra.EntityManagerUtil;
import infra.Logger;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import model.Site;

public class SiteDaoNativeImpl implements SiteDao {

    @Override
    public void create(Site site) throws SiteException {
        Logger.info("SiteDaoNativeImpl.create - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            String sql = "INSERT INTO Site (url, ativo, logo) VALUES (:url, :ativo, :logo)";
            Query query = em.createNativeQuery(sql);
            query.setParameter("url", site.getUrl());
            query.setParameter("ativo", site.getAtivo());
            query.setParameter("logo", site.getLogo());
            query.executeUpdate();
            em.getTransaction().commit();
            Logger.info("SiteDaoNativeImpl.create - sucesso");
        } catch (Exception e) {
            em.getTransaction().rollback();
            Logger.error("SiteDaoNativeImpl.create - erro", e);
            throw new SiteException("Erro ao criar Site", e);
        } finally {
            em.close();
        }
    }

    @Override
    public Site update(Site site) throws SiteException {
        Logger.info("SiteDaoNativeImpl.update - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            String sql = "UPDATE Site SET url=:url, ativo=:ativo, logo=:logo WHERE id_site=:id";
            Query query = em.createNativeQuery(sql);
            query.setParameter("url", site.getUrl());
            query.setParameter("ativo", site.getAtivo());
            query.setParameter("logo", site.getLogo());
            query.setParameter("id", site.getIdSite());
            int updated = query.executeUpdate();
            if (updated == 0) {
                throw new SiteException("Site não encontrado: id=" + site.getIdSite());
            }
            em.getTransaction().commit();
            Logger.info("SiteDaoNativeImpl.update - sucesso");
            return findById(site.getIdSite());
        } catch (SiteException e) {
            em.getTransaction().rollback();
            Logger.error("SiteDaoNativeImpl.update - erro", e);
            throw e;
        } catch (Exception e) {
            em.getTransaction().rollback();
            Logger.error("SiteDaoNativeImpl.update - erro", e);
            throw new SiteException("Erro ao atualizar Site", e);
        } finally {
            em.close();
        }
    }

    @Override
    public void deleteById(Integer id) throws SiteException {
        Logger.info("SiteDaoNativeImpl.deleteById - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            String sql = "DELETE FROM Site WHERE id_site=:id";
            Query query = em.createNativeQuery(sql);
            query.setParameter("id", id);
            int deleted = query.executeUpdate();
            if (deleted == 0) {
                throw new SiteException("Site não encontrado: id=" + id);
            }
            em.getTransaction().commit();
            Logger.info("SiteDaoNativeImpl.deleteById - sucesso");
        } catch (SiteException e) {
            em.getTransaction().rollback();
            Logger.error("SiteDaoNativeImpl.deleteById - erro", e);
            throw e;
        } catch (Exception e) {
            em.getTransaction().rollback();
            Logger.error("SiteDaoNativeImpl.deleteById - erro", e);
            throw new SiteException("Erro ao deletar Site", e);
        } finally {
            em.close();
        }
    }

    @Override
    public Site findById(Integer id) throws SiteException {
        Logger.info("SiteDaoNativeImpl.findById - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_site, url, ativo FROM Site WHERE id_site=:id";
            Query query = em.createNativeQuery(sql, Site.class);
            query.setParameter("id", id);
            Site s = (Site) query.getSingleResult();
            Logger.info("SiteDaoNativeImpl.findById - sucesso");
            return s;
        } catch (Exception e) {
            Logger.error("SiteDaoNativeImpl.findById - erro", e);
            throw new SiteException("Site não encontrado: id=" + id, e);
        } finally {
            em.close();
        }
    }

    @Override
    public Site findWithLogoById(Integer id) throws SiteException {
        Logger.info("SiteDaoNativeImpl.findWithLogoById - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_site, url, ativo, logo FROM Site WHERE id_site=:id";
            Query query = em.createNativeQuery(sql, Site.class);
            query.setParameter("id", id);
            Site s = (Site) query.getSingleResult();
            Logger.info("SiteDaoNativeImpl.findWithLogoById - sucesso");
            return s;
        } catch (Exception e) {
            Logger.error("SiteDaoNativeImpl.findWithLogoById - erro", e);
            throw new SiteException("Site não encontrado: id=" + id, e);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Site> findAll() {
        Logger.info("SiteDaoNativeImpl.findAll - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_site, url, ativo FROM Site";
            Query query = em.createNativeQuery(sql, Site.class);
            List<Site> list = query.getResultList();
            Logger.info("SiteDaoNativeImpl.findAll - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Site> findAll(int page, int size) {
        Logger.info("SiteDaoNativeImpl.findAll(page) - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_site, url, ativo FROM Site LIMIT :limit OFFSET :offset";
            Query query = em.createNativeQuery(sql, Site.class);
            query.setParameter("limit", size);
            query.setParameter("offset", page * size);
            List<Site> list = query.getResultList();
            Logger.info("SiteDaoNativeImpl.findAll(page) - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Site> findByUrl(String url) {
        Logger.info("SiteDaoNativeImpl.findByUrl - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_site, url, ativo FROM Site WHERE url=:url";
            Query query = em.createNativeQuery(sql, Site.class);
            query.setParameter("url", url);
            List<Site> list = query.getResultList();
            Logger.info("SiteDaoNativeImpl.findByUrl - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Site> findByAtivo(Boolean ativo) {
        Logger.info("SiteDaoNativeImpl.findByAtivo - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_site, url, ativo FROM Site WHERE ativo=:ativo";
            Query query = em.createNativeQuery(sql, Site.class);
            query.setParameter("ativo", ativo);
            List<Site> list = query.getResultList();
            Logger.info("SiteDaoNativeImpl.findByAtivo - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Site> findByLogo(byte[] logo) {
        Logger.info("SiteDaoNativeImpl.findByLogo - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_site, url, ativo, logo FROM Site WHERE logo=:logo";
            Query query = em.createNativeQuery(sql, Site.class);
            query.setParameter("logo", logo);
            List<Site> list = query.getResultList();
            Logger.info("SiteDaoNativeImpl.findByLogo - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Site> search(Site filtro) {
        return search(filtro, -1, -1);
    }

    @Override
    public List<Site> search(Site filtro, int page, int size) {
        Logger.info("SiteDaoNativeImpl.search - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            StringBuilder sb = new StringBuilder("SELECT id_site, url, ativo FROM Site WHERE 1=1");
            Map<String, Object> params = new HashMap<>();
            if (filtro.getUrl() != null && !filtro.getUrl().isEmpty()) {
                sb.append(" AND url=:url");
                params.put("url", filtro.getUrl());
            }
            if (filtro.getAtivo() != null) {
                sb.append(" AND ativo=:ativo");
                params.put("ativo", filtro.getAtivo());
            }
            if (filtro.getLogo() != null) {
                sb.append(" AND logo=:logo");
                params.put("logo", filtro.getLogo());
            }
            if (page >= 0 && size > 0) {
                sb.append(" LIMIT :limit OFFSET :offset");
            }
            Query query = em.createNativeQuery(sb.toString(), Site.class);
            for (Map.Entry<String, Object> e : params.entrySet()) {
                query.setParameter(e.getKey(), e.getValue());
            }
            if (page >= 0 && size > 0) {
                query.setParameter("limit", size);
                query.setParameter("offset", page * size);
            }
            List<Site> list = query.getResultList();
            Logger.info("SiteDaoNativeImpl.search - sucesso");
            return list;
        } finally {
            em.close();
        }
    }
}
