// path: src/main/java/model/Site.java
package model;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "Site")
public class Site {

    @Id
    @Column(name = "id_site")
    private Integer idSite;

    @Column(name = "url")
    private String url;

    @Column(name = "ativo")
    private Boolean ativo;

    @Lob
    @Column(name = "logo")
    private byte[] logo;

    public Integer getIdSite() {
        return idSite;
    }

    public void setIdSite(Integer idSite) {
        this.idSite = idSite;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public byte[] getLogo() {
        return logo;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Site)) return false;
        Site site = (Site) o;
        return Objects.equals(idSite, site.idSite);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idSite);
    }

    @Override
    public String toString() {
        return "Site{" +
                "idSite=" + idSite +
                ", url='" + url + '\'' +
                ", ativo=" + ativo +
                '}';
    }
}
