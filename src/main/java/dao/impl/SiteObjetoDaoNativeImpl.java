package dao.impl;

import dao.api.SiteObjetoDao;
import exception.SiteObjetoException;
import infra.EntityManagerUtil;
import infra.Logger;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import model.SiteObjeto;

import java.util.List;

public class SiteObjetoDaoNativeImpl implements SiteObjetoDao {
    private final EntityManager em = EntityManagerUtil.getEntityManager();

    @Override
    public void create(SiteObjeto e) {
        Logger.info("SiteObjetoDaoNativeImpl.create");
        em.getTransaction().begin();
        e.setIdSiteObjeto(null);
        em.persist(e);
        em.getTransaction().commit();
    }

    @Override
    public void update(SiteObjeto e) {
        Logger.info("SiteObjetoDaoNativeImpl.update");
        em.getTransaction().begin();
        em.merge(e);
        em.getTransaction().commit();
    }

    @Override
    public void deleteById(Integer id) {
        Logger.info("SiteObjetoDaoNativeImpl.deleteById");
        SiteObjeto e = em.find(SiteObjeto.class, id);
        if (e == null) throw new SiteObjetoException("SiteObjeto nao encontrado: id=" + id);
        em.getTransaction().begin();
        em.remove(e);
        em.getTransaction().commit();
    }

    @Override
    public SiteObjeto findById(Integer id) {
        String sql = "SELECT id_site_objeto, id_site, id_objeto FROM Site_Objeto WHERE id_site_objeto = :id";
        Query q = em.createNativeQuery(sql, SiteObjeto.class).setParameter("id", id);
        return (SiteObjeto) q.getSingleResult();
    }

    @Override
    public List<SiteObjeto> findAll() {
        String sql = "SELECT id_site_objeto, id_site, id_objeto FROM Site_Objeto";
        return em.createNativeQuery(sql, SiteObjeto.class).getResultList();
    }

    @Override
    public List<SiteObjeto> findAll(int page, int size) {
        String sql = "SELECT id_site_objeto, id_site, id_objeto FROM Site_Objeto LIMIT :size OFFSET :off";
        return em.createNativeQuery(sql, SiteObjeto.class)
                .setParameter("size", size)
                .setParameter("off", page * size)
                .getResultList();
    }

    @Override
    public List<SiteObjeto> findByIdSite(Integer idSite) {
        String sql = "SELECT id_site_objeto, id_site, id_objeto FROM Site_Objeto WHERE id_site = :v";
        return em.createNativeQuery(sql, SiteObjeto.class).setParameter("v", idSite).getResultList();
    }

    @Override
    public List<SiteObjeto> findByIdObjeto(Integer idObjeto) {
        String sql = "SELECT id_site_objeto, id_site, id_objeto FROM Site_Objeto WHERE id_objeto = :v";
        return em.createNativeQuery(sql, SiteObjeto.class).setParameter("v", idObjeto).getResultList();
    }

    @Override
    public List<SiteObjeto> search(SiteObjeto f) {
        return search(f, 0, Integer.MAX_VALUE);
    }

    @Override
    public List<SiteObjeto> search(SiteObjeto f, int page, int size) {
        StringBuilder sb = new StringBuilder("SELECT id_site_objeto, id_site, id_objeto FROM Site_Objeto WHERE 1=1");
        if (f.getIdSite() != null) sb.append(" AND id_site = :idSite");
        if (f.getIdObjeto() != null) sb.append(" AND id_objeto = :idObjeto");
        Query q = em.createNativeQuery(sb.toString(), SiteObjeto.class);
        if (f.getIdSite() != null) q.setParameter("idSite", f.getIdSite());
        if (f.getIdObjeto() != null) q.setParameter("idObjeto", f.getIdObjeto());
        q.setFirstResult(page * size);
        q.setMaxResults(size);
        return q.getResultList();
    }
}