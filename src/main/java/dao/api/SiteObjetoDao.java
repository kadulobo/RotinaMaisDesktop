package dao.api;

import model.SiteObjeto;
import java.util.List;

public interface SiteObjetoDao {
    void create(SiteObjeto e);
    void update(SiteObjeto e);
    void deleteById(Integer id);
    SiteObjeto findById(Integer id);
    List<SiteObjeto> findAll();
    List<SiteObjeto> findAll(int page, int size);
    List<SiteObjeto> findByIdSite(Integer idSite);
    List<SiteObjeto> findByIdObjeto(Integer idObjeto);
    List<SiteObjeto> search(SiteObjeto f);
    List<SiteObjeto> search(SiteObjeto f, int page, int size);
}