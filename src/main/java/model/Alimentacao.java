// path: src/main/java/model/Alimentacao.java
package model;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name = "Alimentacao")
public class Alimentacao {

    @Id
    @Column(name = "id_alimentacao")
    private Integer idAlimentacao;

    @Column(name = "status")
    private Integer status;

    @Column(name = "nome")
    private String nome;

    @Column(name = "link")
    private String link;

    @Lob
    @Column(name = "video")
    private byte[] video;

    @Column(name = "preparo")
    private String preparo;

    @Column(name = "id_rotina")
    private Integer idRotina;

    public Integer getIdAlimentacao() {
        return idAlimentacao;
    }

    public void setIdAlimentacao(Integer idAlimentacao) {
        this.idAlimentacao = idAlimentacao;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public byte[] getVideo() {
        return video;
    }

    public void setVideo(byte[] video) {
        this.video = video;
    }

    public String getPreparo() {
        return preparo;
    }

    public void setPreparo(String preparo) {
        this.preparo = preparo;
    }

    public Integer getIdRotina() {
        return idRotina;
    }

    public void setIdRotina(Integer idRotina) {
        this.idRotina = idRotina;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Alimentacao)) return false;
        Alimentacao that = (Alimentacao) o;
        return Objects.equals(idAlimentacao, that.idAlimentacao);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idAlimentacao);
    }

    @Override
    public String toString() {
        return "Alimentacao{" +
                "idAlimentacao=" + idAlimentacao +
                ", status=" + status +
                ", nome='" + nome + '\'' +
                ", link='" + link + '\'' +
                ", preparo='" + preparo + '\'' +
                ", idRotina=" + idRotina +
                '}';
    }
}
