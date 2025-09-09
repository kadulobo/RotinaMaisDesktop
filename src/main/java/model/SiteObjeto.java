package model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "Site_Objeto")
public class SiteObjeto {
    @Id
    @Column(name = "id_site_objeto")
    private Integer idSiteObjeto;

    
    @Column(name = "id_site")
    private Integer idSite;

    
    @Column(name = "id_objeto")
    private Integer idObjeto;

    public Integer getIdSiteObjeto() { return idSiteObjeto; }
    public void setIdSiteObjeto(Integer idSiteObjeto) { this.idSiteObjeto = idSiteObjeto; }

    public Integer getIdSite() { return idSite; }
    public void setIdSite(Integer idSite) { this.idSite = idSite; }

    public Integer getIdObjeto() { return idObjeto; }
    public void setIdObjeto(Integer idObjeto) { this.idObjeto = idObjeto; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SiteObjeto that = (SiteObjeto) o;
        return Objects.equals(idSiteObjeto, that.idSiteObjeto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idSiteObjeto);
    }

    @Override
    public String toString() {
        return "SiteObjeto{" +
                "idSiteObjeto=" + idSiteObjeto +
                ", idSite=" + idSite +
                ", idObjeto=" + idObjeto +
                '}';
    }
}