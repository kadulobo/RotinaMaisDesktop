// path: src/main/java/dao/api/SiteDao.java
package dao.api;

import java.util.List;

import exception.SiteException;
import model.Site;

public interface SiteDao {

    void create(Site site) throws SiteException;

    Site update(Site site) throws SiteException;

    void deleteById(Integer id) throws SiteException;

    Site findById(Integer id) throws SiteException;

    Site findWithLogoById(Integer id) throws SiteException;

    List<Site> findAll();

    List<Site> findAll(int page, int size);

    List<Site> findByUrl(String url);

    List<Site> findByAtivo(Boolean ativo);

    List<Site> findByLogo(byte[] logo);

    List<Site> search(Site filtro);

    List<Site> search(Site filtro, int page, int size);
}
